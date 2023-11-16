package edu.skku.cs.skkedula.fragments.map

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.gson.annotations.SerializedName
import edu.skku.cs.skkedula.LoginActivity
import edu.skku.cs.skkedula.R
import edu.skku.cs.skkedula.api.ApiObject
import edu.skku.cs.skkedula.api.Deletepath
import edu.skku.cs.skkedula.api.DeletepathResponse
import edu.skku.cs.skkedula.api.LoginResponse
import edu.skku.cs.skkedula.api.Savepath
import edu.skku.cs.skkedula.api.SavepathResponse
import edu.skku.cs.skkedula.databinding.FragmentRouteresultBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RouteresultFragment : Fragment() {
    private lateinit var _binding: FragmentRouteresultBinding
    private val binding get() = _binding!!
    private val mapViewModel: MapViewModel by activityViewModels()
    private var startString: String = " "
    private var endString: String = " "

    private fun applyDarkEffect(bookmarkbutton:ImageView) {
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f)
        val colorFilter = ColorMatrixColorFilter(colorMatrix)
        bookmarkbutton.colorFilter = colorFilter
    }
    private fun removeDarkEffect(bookmarkbutton:ImageView) {
        bookmarkbutton.colorFilter = null
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRouteresultBinding.inflate(inflater, container, false)
        var userId = LoginActivity.loginData.userId

        // Bookmarkbutton control area
        val bookmarkbutton = binding.onbookmarkButton
        var state = 0
        applyDarkEffect(bookmarkbutton)
        bookmarkbutton.setOnClickListener {
            //todo: add start, end at db
            if (state == 0) {
                val saveObject = Savepath(
                    ID = userId,
                    Sequence= 1,
                    Stopover_count = 0,
                    Start_latitude = 37.29539,
                    Start_longitude = 126.97630,
                    End_latitude = 37.29539,
                    End_longitude = 126.97630,
                    Stopover = "_"
                )
                val addBookmark_ = ApiObject.service.savePath(saveObject)
                addBookmark_.clone().enqueue(object: Callback<SavepathResponse> {
                    override fun onResponse(call: Call<SavepathResponse>, response: Response<SavepathResponse>) {
                        val savepathresponse = response.body()
                        val statusmessage = savepathresponse?.message

                        if(response.isSuccessful.not()){
                            Log.d("DATA", "code=${response.code()}, message = $statusmessage")
                            return
                        }
                        Log.d("DATA", "code=${response.code()}, message = $statusmessage")
                    }
                    override fun onFailure(call: Call<SavepathResponse>, t: Throwable) {
                        Log.e("ERROR", t.toString())
                    }
                })
                showToast("즐겨찾기에 경로가 추가되었습니다.")
                removeDarkEffect(bookmarkbutton)
                state=1
            }
            //todo: delete start, end at db
            else {
                val deleteObject = Deletepath(
                    ID= userId,
                    Sequence = 1
                )
                val deleteBookmark_ = ApiObject.service.deletePath(deleteObject)
                deleteBookmark_.clone().enqueue(object: Callback<DeletepathResponse> {
                    override fun onResponse(call: Call<DeletepathResponse>, response: Response<DeletepathResponse>) {
                        val deletepathResponse = response.body()
                        val statusmessage = deletepathResponse?.message

                        if(response.isSuccessful.not()){
                            Log.d("DATA", "code=${response.code()}, message = $statusmessage")
                            return
                        }
                        Log.d("DATA", "code=${response.code()}, message = $statusmessage")
                    }
                    override fun onFailure(call: Call<DeletepathResponse>, t: Throwable) {
                        Log.e("ERROR", t.toString())
                    }
                })
                showToast("즐겨찾기에 경로가 삭제되었습니다.")
                applyDarkEffect(bookmarkbutton)
                state=0
            }
        }

        // Entrancebutton control area
        val entrancebutton = binding.entrance2
        entrancebutton.setOnClickListener {

        }

        val rootView = binding.root
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapViewModel.startText.observe(viewLifecycleOwner) { text ->
            val editstart = view.findViewById<TextView>(R.id.startText)
            editstart.text = text
            startString = text
        }
        mapViewModel.endText.observe(viewLifecycleOwner) { text ->
            val editend = view.findViewById<TextView>(R.id.endText)
            editend.text = text
            endString = text
        }
    }
}
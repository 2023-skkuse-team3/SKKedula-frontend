package edu.skku.cs.skkedula.fragments.bookmark

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import edu.skku.cs.skkedula.LoginActivity
import edu.skku.cs.skkedula.R
import edu.skku.cs.skkedula.api.ApiObject
import edu.skku.cs.skkedula.api.Getpath
import edu.skku.cs.skkedula.api.GetpathResponse
import edu.skku.cs.skkedula.api.Savepath
import edu.skku.cs.skkedula.api.SavepathResponse
import edu.skku.cs.skkedula.databinding.FragmentBookmarkBinding
import edu.skku.cs.skkedula.fragments.map.RouteresultFragment
import edu.skku.cs.skkedula.fragments.map.RoutesearchFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookmarkFragment : Fragment() {
    private var _binding: FragmentBookmarkBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        val rootView = binding.root
        rootView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))

        var items = arrayListOf<Bookmark>()

        var userId = LoginActivity.loginData.userId
        //Log.d("DATA", "-----${userId}------")
        items.add(Bookmark("26102", "85712", "-", "-", "-", "-", "-"))

        val listAdapter = BookmarkAdapter(requireContext(), items)
        val mainList = binding.scrollview
        mainList.adapter = listAdapter

        mainList.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = items[position]
            // todo: go to RouteresultFragment with start, end string
            val getObject = Getpath(
                ID = userId,
                Sequence= 1
            )
            val getBookmark_ = ApiObject.service.getPath(getObject)
            getBookmark_.clone().enqueue(object: Callback<GetpathResponse> {
                override fun onResponse(call: Call<GetpathResponse>, response: Response<GetpathResponse>) {
                    val getpathresponse = response.body()

                    if(response.isSuccessful.not()){
                        Log.d("DATA", "code=${response.code()}")
                        return
                    }
                    Log.d("DATA", "code=${response.code()}")

                    val sla = getpathresponse?.Start_latitude
                    val slo = getpathresponse?.End_latitude
                    val ela = getpathresponse?.End_latitude
                    val elo = getpathresponse?.End_longitude
                    Log.d("DATA", "${sla},${slo},${ela},${elo}")
                }
                override fun onFailure(call: Call<GetpathResponse>, t: Throwable) {
                    Log.e("ERROR", t.toString())
                }
            })
            //selectedItem.start
            //selectedItem.end
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment_activity_main, RouteresultFragment())
            //transaction.addToBackStack("fragment_routesearch")
            transaction.commit()
        }

        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
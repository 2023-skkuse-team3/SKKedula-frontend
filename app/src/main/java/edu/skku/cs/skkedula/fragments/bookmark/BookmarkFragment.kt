package edu.skku.cs.skkedula.fragments.bookmark

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.naver.maps.geometry.LatLng
import edu.skku.cs.skkedula.LoginActivity
import edu.skku.cs.skkedula.R
import edu.skku.cs.skkedula.api.ApiObject
import edu.skku.cs.skkedula.api.Getpath
import edu.skku.cs.skkedula.api.GetpathResponse
import edu.skku.cs.skkedula.api.Savepath
import edu.skku.cs.skkedula.api.SavepathResponse
import edu.skku.cs.skkedula.databinding.FragmentBookmarkBinding
import edu.skku.cs.skkedula.fragments.map.MapFragment
import edu.skku.cs.skkedula.fragments.map.RouteresultFragment
import edu.skku.cs.skkedula.fragments.map.RouteresultFragment2
import edu.skku.cs.skkedula.fragments.map.RoutesearchFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookmarkFragment : Fragment() {
    private var _binding: FragmentBookmarkBinding? = null
    private val bookmarkViewModel: BookmarkViewModel by activityViewModels()

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

        var items = bookmarkViewModel.items
        var userId = LoginActivity.loginData.userId

        // 26310 to 23217:
        // start: 37.29501652, 126.9773477
        // stopover1: 37.29485663219833, 126.97587565874039
        // stopover2: 37.29412794877511, 126.97576437025737
        // end: 37.294301, 126.976732
        val startLat = LatLng(37.29485663219833, 126.97587565874039)
        val endLat = LatLng(37.294301, 126.976732)
        val stopoverList = mutableListOf(LatLng(37.29485663219833, 126.97587565874039), LatLng(37.29412794877511, 126.97576437025737))
        if (bookmarkViewModel.state == 0) {
            items.add(Bookmark("26310", "23217", startLat, endLat, stopoverList))
            bookmarkViewModel.state++
        }

        Log.d("DATA", "${items.size}")

        val listAdapter = BookmarkAdapter(requireContext(), items)
        val mainList = binding.scrollview
        mainList.adapter = listAdapter

        mainList.setOnItemClickListener { parent, view, position, id ->
            val Item = items[position]


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
            transaction.replace(R.id.nav_host_fragment_activity_main, RouteresultFragment2())

            transaction.commit()
        }

        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
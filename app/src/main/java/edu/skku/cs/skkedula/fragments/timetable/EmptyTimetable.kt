package edu.skku.cs.skkedula.fragments.timetable

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil.isValidUrl
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import edu.skku.cs.skkedula.LoginActivity
import edu.skku.cs.skkedula.R
import edu.skku.cs.skkedula.api.ApiObject8000
import edu.skku.cs.skkedula.api.Course
import edu.skku.cs.skkedula.api.Login
import edu.skku.cs.skkedula.api.UserId
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EmptyTimetable.newInstance] factory method to
 * create an instance of this fragment.
 */
class EmptyTimetable : Fragment() {

    private val timetableViewModel: TimetableViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_empty_timetable, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 버튼 클릭 시 Fragment 전환
        val button = view.findViewById<Button>(R.id.addDirectly)
        val urlButton = view.findViewById<Button>(R.id.addWithUrl)

        button.setOnClickListener {

            timetableViewModel.setExampleTimetable()

            // timetable edit button 표시
            val editButton = requireActivity().findViewById<ImageButton>(R.id.editButton)
            editButton?.visibility = View.VISIBLE

            // timetable fragment로 교체
            val timetable = Timetable()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, timetable)
                .commit()
        }

        urlButton.setOnClickListener {
            // url 가져오기
            val urlInput = view.findViewById<EditText>(R.id.InputUrl)

            val url = urlInput.text.toString()
            val isValid = isValidUrl(url)
            Log.d("URL", "$isValid")

            // 유효한 URL일 때
            if (isValidUrl(urlInput.text.toString())) {
                // user id 가져오기
                // var userId = LoginActivity.loginData.userId
                // Log.d( "userId", userId)
                // api interface 가져오기
                val postUserCourses = ApiObject8000.service.postUserCourses(UserId("1"), url)
                Log.d("API", "api 호출")
                // url post
                postUserCourses.clone().enqueue(object: Callback<List<Course>> {
                    override fun onResponse(call: Call<List<Course>>, response: Response<List<Course>>) {
                        if(response.isSuccessful.not()){
                            return
                        }

                        response.body()?.let{
                            Log.d("OK", it.toString())
                            it.forEachIndexed { index, course ->
                                // 불러온 강의를 dataview에 추가
                                timetableViewModel.addNewCourseToTimetable(course)
                                Log.d("DATA", "[$index] date = ${course.courseName}, name = ${course.professor}")
                            }

                            // timetable fragment로 교체
                            val timetable = Timetable()
                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.fragmentContainerView, timetable)
                                .commit()

                            // timetable edit button 표시
                            val editButton = requireActivity().findViewById<ImageButton>(R.id.editButton)
                            editButton?.visibility = View.VISIBLE

                        } ?: run {
                            Log.d("NG", "body is null")
                        }
                    }

                    override fun onFailure(call: Call<List<Course>>, t: Throwable) {
                        Log.e("ERROR", t.toString())
                        Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show()
                    }

                })


                // 사용자의 강의 목록 불러와 시간표에 추가하기
            } else {
                // 유효하지 않은 URL입니다. 사용자에게 알림 또는 처리를 수행하세요.
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EmptyTimetable.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EmptyTimetable().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
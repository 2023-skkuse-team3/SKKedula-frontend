package edu.skku.cs.skkedula.fragments.timetable

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import edu.skku.cs.skkedula.api.ApiObject
import edu.skku.cs.skkedula.api.Course
import edu.skku.cs.skkedula.api.CourseName
import edu.skku.cs.skkedula.api.Professor
import edu.skku.cs.skkedula.databinding.FragmentCourseSearchBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CourseSearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CourseSearchFragment : Fragment() {
    // TODO: Rename and change types of parameters

    lateinit var binding: FragmentCourseSearchBinding
    private var courseList:List<Course> = emptyList()

    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentCourseSearchBinding.inflate(inflater)

        // radio 버튼
        val courseRadio = binding.courseRadio
        val professorRadio = binding.professorRadio

        // 강의명 버튼을 default로 설정
        courseRadio.isChecked = true

        // 강의 리스트 View
        val courseListView = binding.recyclerView

        fun callApi (input: String) {
            Log.d("TextChanged", "Api 호출")

            var callSearchCourse = ApiObject.service.searchCourse(CourseName(input))

            // 교수명 선택시
            if (professorRadio.isChecked) {
                // api interface 가져오기
                callSearchCourse = ApiObject.service.searchCourseByProfessor(Professor(input))
            }

            job?.cancel()

            job = CoroutineScope(Dispatchers.Main).launch {
                callSearchCourse.clone().enqueue(object: Callback<List<Course>> {

                    override fun onResponse(call: Call<List<Course>>, response: Response<List<Course>>) {
                        if(response.isSuccessful.not()){
                            return
                        }

                        response.body()?.let{
                            Log.d("OK", it.toString())
                            // courseList 초기화
                            val tempCourseList:MutableList<Course> = mutableListOf()

                            it.forEachIndexed { index, course ->
                                tempCourseList.add(index, course)
                            }

                            courseList = tempCourseList

                            val adapter = RecyclerViewAdapter(courseList){
                                course -> Toast.makeText(requireActivity(), "${course.courseId} 클릭!", Toast.LENGTH_SHORT).show()
                            }
                            adapter.notifyDataSetChanged()

                            courseListView.adapter = adapter
                            courseListView.layoutManager = LinearLayoutManager(requireContext())


                            Log.d("LIST", "$tempCourseList")

                        } ?: run {
                            Log.d("NG", "body is null")
                        }
                    }

                    override fun onFailure(call: Call<List<Course>>, t: Throwable) {
                        Log.e("ERROR", t.toString())
                        Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show()
                    }

                })
            }
        }


        val searchInput = binding.searchInput
        // input 값 바뀔 때마다 api 호출
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 텍스트 변경 전
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 텍스트 변경 중
            }

            override fun afterTextChanged(s: Editable?) {
                // 텍스트 변경 후, API 호출 등의 작업 수행
                callApi(s.toString())
            }
        })

        // radio 버튼을 변경할 때도 api 호출
        val radioGroup = binding.radioGroup
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            callApi(searchInput.text.toString())
        }

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CourseSearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CourseSearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
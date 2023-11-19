package edu.skku.cs.skkedula.fragments.timetable

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.activityViewModels
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import androidx.transition.Visibility
import com.google.android.material.button.MaterialButton
import edu.skku.cs.skkedula.LoginActivity
import edu.skku.cs.skkedula.R
import edu.skku.cs.skkedula.api.ApiObject
import edu.skku.cs.skkedula.api.Message
import edu.skku.cs.skkedula.api.UserCourse
import edu.skku.cs.skkedula.fragments.map.MapViewModel
import edu.skku.cs.skkedula.fragments.map.RoutesearchFragment
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 * Use the [CourseDetail.newInstance] factory method to
 * create an instance of this fragment.
 */
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CourseDetail : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val timetableViewModel: TimetableViewModel by activityViewModels()
    private val mapViewModel: MapViewModel by activityViewModels()

    private var startLocation = "현위치"
    private var endLocation = ""

    // user id 가져오기
    private var userId = LoginActivity.loginData.userId
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 출발지
        val startPoint = view.findViewById<TextView>(R.id.startPointValue)
        // 도착지
        val destinationLabel = view.findViewById<TextView>(R.id.destinationInfo)

        startPoint.setOnClickListener {
            // startpoint 클릭 활성화
            timetableViewModel.enableStartPoint()

            // card view 살짝 내리기
            val cardView = requireActivity().findViewById<FragmentContainerView>(R.id.card)
            val startPoint = view.findViewById<TextView>(R.id.startPointValue)
            val constraintLayout = requireActivity().findViewById<ConstraintLayout>(R.id.main_constraint)

            // 아래로 100dp 이동하는 애니메이션
            val translateY = 100.dpToPx().toFloat()

            // ConstraintSet을 생성하고 시작 상태를 설정
            val startConstraintSet = ConstraintSet()
            startConstraintSet.clone(constraintLayout)

            // ConstraintSet을 생성하고 종료 상태를 설정
            val endConstraintSet = ConstraintSet()
            endConstraintSet.clone(constraintLayout)
            endConstraintSet.connect(cardView.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, -500)
            //endConstraintSet.setVerticalBias(cardView.id, translateY)

            // Transition을 설정하여 애니메이션 적용
            val transition = ChangeBounds()
            transition.duration = 500 // 애니메이션 지속 시간 (밀리초)
            TransitionManager.beginDelayedTransition(constraintLayout, transition)

            // 종료 상태의 ConstraintSet을 적용하여 애니메이션 적용
            endConstraintSet.applyTo(constraintLayout)
            Log.d("Animation", "card down")
        }

        // 도착지 string 초기화
        var destination = ""

        // 강의실 정보 표시
        timetableViewModel.tempClassroom.observe(viewLifecycleOwner) { text ->
            val classroom = view.findViewById<TextView>(R.id.classroom)
            classroom.text = text
            destination += text
            endLocation = text.split(" ")[text.split(" ").size - 1]
        }

        // 강의명 표시
        timetableViewModel.tempCourseName.observe(viewLifecycleOwner) { text ->
            val courseName = view.findViewById<TextView>(R.id.courseName)
            courseName.text = text

            // 도착지 표시
            destination += "($text)"
            destinationLabel.text = destination
        }

        // 교수명 표시
        timetableViewModel.tempProfessor.observe(viewLifecycleOwner) { text ->
            val professor = view.findViewById<TextView>(R.id.professor)
            professor.text = text
        }

        // 강의 시간 표시
        timetableViewModel.tempCourseTime.observe(viewLifecycleOwner) { text ->
            val time = view.findViewById<TextView>(R.id.time)
            time.text = text
        }

        // 강의 유형 표시
        timetableViewModel.tempCourseType.observe(viewLifecycleOwner) { text ->
            val courseType = view.findViewById<TextView>(R.id.courseType)
            courseType.text = text
        }

        // 출발지 표시
        timetableViewModel.startPoint.observe(viewLifecycleOwner) { text ->
            val startPoint = view.findViewById<TextView>(R.id.startPointValue)
            startPoint.text = text
            if (!startLocation.contains("현위치"))
                startLocation = text.split(" ")[text.split(" ").size - 1]
        }

        // 강의 삭제 버튼
        val removeBtn = view.findViewById<ImageButton>(R.id.removeButton)
        removeBtn.setOnClickListener {
            // viewmodel에서 해당 강의 삭제
            var courseId = ""
            timetableViewModel.tempCourseId.value?.let { it1 -> courseId = it1}
            timetableViewModel.removeCourse(courseId)

            // 강의 삭제 api 호출
            val callRemoveCourse = ApiObject.service.removeCourseFromTimetable(UserCourse(userId, courseId))
            callRemoveCourse.clone().enqueue(object: Callback<Message> {
                override fun onResponse(call: Call<Message>, response: Response<Message>) {
                    response.body()?.let{
                        Log.d("OK", it.toString())

                    } ?: run {
                        Log.d("NG", "body is null")
                    }
                }

                override fun onFailure(call: Call<Message>, t: Throwable) {
                    Log.e("ERROR", t.toString())
                    Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show()
                }
            })

            // 카드 내리기
            val cardView = requireActivity().findViewById<FragmentContainerView>(R.id.card)
            val buttomDownAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.bottom_down)
            cardView.startAnimation(buttomDownAnimation)
            cardView.visibility = View.GONE
        }

        // 강의실 위치 찾기 버튼
        val searchBtn = view.findViewById<MaterialButton>(R.id.searchButton)
        searchBtn.setOnClickListener {
            // map view model에 출발/도착지 정보 저장
            mapViewModel.startText.value = startPoint.text.toString()
            mapViewModel.endText.value = destinationLabel.text.toString()
            mapViewModel.endLocation.value = endLocation
            mapViewModel.startLocation.value = startLocation

            Log.d("mapViewModel", mapViewModel.startLocation.value + " " + mapViewModel.endLocation.value)

            // 카드 내리기
            val cardView = requireActivity().findViewById<FragmentContainerView>(R.id.card)
            val buttomDownAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.bottom_down)
            cardView.startAnimation(buttomDownAnimation)
            cardView.visibility = View.GONE

            // 화면 전환
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment_activity_main, RoutesearchFragment())
            transaction.commit()
        }
    }

    // dp를 픽셀로 변환하는 확장 함수
    private fun Int.dpToPx(): Int {
        val scale = resources.displayMetrics.density
        return (this * scale + 0.5f).toInt()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LectureDetail.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CourseDetail().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
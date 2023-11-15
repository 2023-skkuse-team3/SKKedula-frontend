package edu.skku.cs.skkedula.fragments.timetable

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.activityViewModels
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import edu.skku.cs.skkedula.R
import edu.skku.cs.skkedula.fragments.map.MapViewModel
import org.w3c.dom.Text

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

        val startPoint = view.findViewById<TextView>(R.id.startPointValue)

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
        }

        // 강의명 표시
        timetableViewModel.tempCourseName.observe(viewLifecycleOwner) { text ->
            val courseName = view.findViewById<TextView>(R.id.courseName)
            courseName.text = text
            destination += "($text)"

            // 도착지 표시
            val destinationLabel = view.findViewById<TextView>(R.id.destinationInfo)
            destinationLabel.text = destination
        }

        // 출발지 표시
        timetableViewModel.startPoint.observe(viewLifecycleOwner) { text ->
            val startPoint = view.findViewById<TextView>(R.id.startPointValue)
            startPoint.text = text
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
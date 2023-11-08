package edu.skku.cs.skkedula.fragments.timetable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
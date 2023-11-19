package edu.skku.cs.skkedula.fragments.timetable

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import edu.skku.cs.skkedula.R
import edu.skku.cs.skkedula.api.Course
import edu.skku.cs.skkedula.api.CourseInfo
import edu.skku.cs.skkedula.databinding.FragmentCustomScheduleBinding
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CustomScheduleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CustomScheduleFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var startTimeString = ""
    var endTimeString = ""
    var timeString = ""
    val days: Map<String, Int> = mapOf(
        "월요일" to 1,
        "화요일" to 2,
        "수요일" to 3,
        "목요일" to 4,
        "금요일" to 5
    )

    lateinit var binding:FragmentCustomScheduleBinding
    private val timetableViewModel: TimetableViewModel by activityViewModels()

    // time string을 1_12001300 형태로 바꿈
    fun formatTimeString(day: String, start: String, end: String): String {
        val dayNum = days[day]

        return dayNum.toString() + "_" + start.replace(":", "") + end.replace(":", "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // val view = inflater.inflate(R.layout.fragment_custom_schedule, container, false)
        binding=FragmentCustomScheduleBinding.inflate(inflater)
        val scheduleName = binding.scheduleName
        val location = binding.location
        val startTime = binding.startTime
        val endTime = binding.endTime
        val day = binding.daySpinner
        val addBtn = binding.addSchedule

        startTime.setOnClickListener {
            // 시간 선택 dialog 표시
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                // 시작 시간 저장
                startTimeString = "${hourOfDay}:${minute}"

                // 시간 textview에 표시
                val startTime2 = binding.startTime
                startTime2.text = startTimeString + " 부터"
            }
            TimePickerDialog(activity, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()
        }

        endTime.setOnClickListener {
            // 시간 선택 dialog 표시
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                // 종료 시간 저장
                endTimeString = "${hourOfDay}:${minute}"

                // 시간 textview에 표시
                val endTime2 = binding.endTime
                endTime2.text = endTimeString + " 까지"
            }
            TimePickerDialog(activity, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()
        }

        // 추가하기 버튼
        addBtn.setOnClickListener {
            if (!scheduleName.text.toString().isNullOrEmpty() && !location.text.toString().isNullOrEmpty() && startTimeString.isNotEmpty() && endTimeString.isNotEmpty()) {
                // 제목과 장소, 시간을 모두 입력했을 때
                try {
                    val roomNum = location.text.toString().toInt()

                    // 커스텀 강의 추가 api 호출



                    val newSchedule = Course(courseId = "", courseName = scheduleName.text.toString(), professor = "", time = "", roomNum = location.text.toString(), classType = "커스텀 일정", semester = "2", year = 2023)

                    // 강의 추가 API 호출, 성공 시 viewmodel에 추가
                    timetableViewModel.addNewCourseToTimetable(newSchedule)
                } catch (e: NumberFormatException) {
                    Toast.makeText(requireContext(), "유효한 강의실 번호가 아닙니다.", Toast.LENGTH_SHORT).show()
                }

                Log.d("tag", "not empty")
            } else {
                Toast.makeText(requireContext(), "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CustomScheduleFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
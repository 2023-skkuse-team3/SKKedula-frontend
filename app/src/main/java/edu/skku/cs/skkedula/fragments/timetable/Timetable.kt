package edu.skku.cs.skkedula.fragments.timetable

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.islandparadise14.mintable.MinTimeTableView
import com.islandparadise14.mintable.model.ScheduleDay
import com.islandparadise14.mintable.model.ScheduleEntity
import com.islandparadise14.mintable.tableinterface.OnScheduleClickListener
import edu.skku.cs.skkedula.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Timetable.newInstance] factory method to
 * create an instance of this fragment.
 */
class Timetable : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val day = arrayOf("월", "화", "수", "목", "금")
    private val colorList: List<String> = listOf(
        "#F28585", "#F2B263", "#86A69D", "#6d9dc9", "#9580bf", "#e38fc4"
    )

    private val timetableViewModel: TimetableViewModel by activityViewModels()
    val scheduleList: ArrayList<ScheduleEntity> = ArrayList()
    fun setTempCourseInfo(courseName: String, classroom: String) {
        timetableViewModel.setTempCourseInfo(courseName, classroom)
    }

    private fun formatTimeString(input: String): String {
        if (input == null) {
            return "미지정"
        }
        if (input.contains(',')) {
            val parts = input.split(", ")
            val formattedTimes = parts.map { part ->
                val (count, times) = part.split("_")
                val dayString = when (count.toInt()) {
                    1 -> "월"
                    2 -> "화"
                    3 -> "수"
                    4 -> "목"
                    5 -> "금"
                    else -> " "
                }
                val startTime = times.substring(0, 2) + ":" + times.substring(2, 4)
                val endTime = times.substring(4, 6) + ":" + times.substring(6, 8)
                "$dayString $startTime-$endTime"
            }
            Log.d("FormattedTimes", formattedTimes.joinToString("/ "))
            return formattedTimes.joinToString("/ ")
        } else if (input.length > 9) {
            var formattedString = input.replace("1_", "월 ")
            formattedString = formattedString.replace("2_", "화 ")
            formattedString = formattedString.replace("3_", "수 ")
            formattedString = formattedString.replace("4_", "목 ")
            formattedString = formattedString.replace("5_", "금 ")
            formattedString = formattedString.substring(0, 4) + ":" + formattedString.substring(4, 6) + "-" + formattedString.substring(6, 8) + ":" + formattedString.substring(8, 10)
            Log.d("FormattedTimes", formattedString)
            return formattedString
        } else {
            return input
        }
    }

    private fun formatRoomNum(input: String): String {
        if (input.length == 5) {
            val buildingName = when (input.substring(0,2)) {
                "21" -> "제1공학관 "
                "22" -> "제1공학관 "
                "23" -> "제1공학관 "
                "25" -> "제2공학관 "
                "26" -> "제2공학관 "
                "27" -> "제2공학관 "
                "85" -> "산학협력센터 "
                else -> ""
            }

            if (buildingName.isNotEmpty()) {
                val floor = when (input[2]) {
                    '1' -> "1층 "
                    '2' -> "2층 "
                    '3' -> "3층 "
                    '4' -> "4층 "
                    '5' -> "5층 "
                    '6' -> "6층 "
                    '7' -> "7층 "
                    else -> ""
                }

                return buildingName + floor + input
            }

            return input
        } else if (input.length == 6) {
            val buildingName = when (input.substring(0,2)) {
                "40" -> "반도체관 "
                "33" -> "화학관 "
                else -> ""
            }

            if (buildingName.isNotEmpty()) {
                val floor = when (input[3]) {
                    '1' -> "1층 "
                    '2' -> "2층 "
                    '3' -> "3층 "
                    '4' -> "4층 "
                    '5' -> "5층 "
                    '6' -> "6층 "
                    '7' -> "7층 "
                    else -> ""
                }

                return buildingName + floor + input
            }

            return input
        }

        return input
    }

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
        val view = inflater.inflate(R.layout.fragment_timetable, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val table = view.findViewById<MinTimeTableView>(R.id.table)
        table.initTable(day)

        timetableViewModel.userCourseList.observe(viewLifecycleOwner) { courseList ->
            Log.d("Function", "observe userCourseList")
            scheduleList.clear()
            Log.d("DATA", "Course List $courseList")
            courseList.mapIndexed { index, course ->
                val backgroundColor = colorList[index % colorList.size]

                val formattedTimeString = formatTimeString(course.time)
                Log.d("DATA", "$course")
                formattedTimeString.split("/ ").map {

                    val day = when(it.substring(0, 1)) {
                        "월" -> ScheduleDay.MONDAY
                        "화" -> ScheduleDay.TUESDAY
                        "수" -> ScheduleDay.WEDNESDAY
                        "목" -> ScheduleDay.THURSDAY
                        "금" -> ScheduleDay.FRIDAY
                        else -> ScheduleDay.MONDAY
                    }

                    Log.d("it", it)

                    val schedule = ScheduleEntity(
                        1, //originId
                        course.courseName, //scheduleName
                        formatRoomNum(course.roomNum), //roomInfo
                        day, //ScheduleDay object (MONDAY ~ SUNDAY)
                        it.substring(2,7), //startTime format: "HH:mm"
                        it.substring(8,13), //endTime  format: "HH:mm"
                        backgroundColor, //backgroundColor (optional)
                        "#FFFFFF" //textcolor (optional)
                    )

                    scheduleList.add(schedule)
                }
            }
            Log.d("DATA", "$scheduleList")
            table.updateSchedules(scheduleList)
        }

        val buttomUpAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.bottom_up)
        val targetView = requireActivity().findViewById<FragmentContainerView>(R.id.card)
        table.setOnScheduleClickListener(
            object : OnScheduleClickListener {
                override fun scheduleClicked(entity: ScheduleEntity) {

                    if (timetableViewModel.isSelectingStartPoint.value == true) {
                        // 출발지 설정하는 경우
                        // 출발지 string 설정
                        timetableViewModel.setStartPoint(entity.roomInfo)

                        // 출발지 설정 후 disable
                        timetableViewModel.disableStartPoint()

                        // 카드 올리기 start
                        val cardView = requireActivity().findViewById<FragmentContainerView>(R.id.card)
                        val constraintLayout = requireActivity().findViewById<ConstraintLayout>(R.id.main_constraint)

                        // ConstraintSet을 생성하고 시작 상태를 설정
                        val startConstraintSet = ConstraintSet()
                        startConstraintSet.clone(constraintLayout)

                        // ConstraintSet을 생성하고 종료 상태를 설정
                        val endConstraintSet = ConstraintSet()
                        endConstraintSet.clone(constraintLayout)
                        endConstraintSet.connect(cardView.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)

                        // Transition을 설정하여 애니메이션 적용
                        val transition = ChangeBounds()
                        transition.duration = 500 // 애니메이션 지속 시간 (밀리초)
                        TransitionManager.beginDelayedTransition(constraintLayout, transition)

                        // 종료 상태의 ConstraintSet을 적용하여 애니메이션 적용
                        endConstraintSet.applyTo(constraintLayout)
                        // 카드 올리기 end

                    } else {
                        // 강의 정보 카드 띄우는 경우
                        // 출발지 string 초기화
                        timetableViewModel.setStartPoint("시간표에서 현위치 선택하기(기본: 현위치)")
                        // apply navigation bar
                        val navController = Navigation.findNavController(requireActivity(), R.id.card)
                        navController.navigate(R.id.courseDetail)

                        // card에 띄울 강의 정보 설정
                        setTempCourseInfo(entity.scheduleName, entity.roomInfo)

                        // card 띄우기
                        targetView.startAnimation(buttomUpAnimation)
                        targetView.visibility = View.VISIBLE
                    }
                }
            }
        )
        table.updateSchedules(scheduleList)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Timetable.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Timetable().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
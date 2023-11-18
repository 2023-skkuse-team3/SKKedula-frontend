package edu.skku.cs.skkedula.fragments.timetable

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import edu.skku.cs.skkedula.api.Course
import edu.skku.cs.skkedula.api.CourseInfo

class TimetableViewModel : ViewModel() {
    private val _tempCourseName = MutableLiveData<String>("")
    val tempCourseName: LiveData<String> = _tempCourseName

    private val _tempClassroom = MutableLiveData<String>("")
    val tempClassroom: LiveData<String> = _tempClassroom

    private val _startPoint = MutableLiveData<String>("시간표에서 출발지 선택하기(기본: 현위치)")
    val startPoint: LiveData<String> = _startPoint

    private val _userCourseList = MutableLiveData<MutableList<Course>>(mutableListOf())
    val userCourseList: LiveData<MutableList<Course>> = _userCourseList

    private val _isSelectingStartPoint = MutableLiveData<Boolean>(false)
    val isSelectingStartPoint: LiveData<Boolean> = _isSelectingStartPoint

    fun setTempCourseInfo(name: String, classroom: String) {
        _tempCourseName.value = name
        _tempClassroom.value = classroom
    }

    fun addNewCourseToTimetable(newItem: Course) {
        // 현재의 리스트를 가져와서 수정
        val currentList = _userCourseList.value ?: mutableListOf()

        // 영어 강의명 삭제
        newItem.courseName = newItem.courseName.split("\n")[0]

        // 같은 요일의 시간표를 합치기 위한 변수
        var time = newItem.time
        var mon = ""
        var tue = ""
        var wed = ""
        var thu = ""
        var fri = ""

        // 같은 요일의 시간을 하나로 합침
        if (time.contains(",")) {
            time.split(", ").map {
                when (it[0]) {
                    '1' ->
                        mon = if (mon.isEmpty()) it
                        else mon.substring(0, 6) + it.substring(6, 10)
                    '2' ->
                        tue = if (tue.isEmpty()) it
                        else tue.substring(0, 6) + it.substring(6, 10)
                    '3' ->
                        wed = if (wed.isEmpty()) it
                        else wed.substring(0, 6) + it.substring(6, 10)
                    '4' ->
                        thu = if (thu.isEmpty()) it
                        else thu.substring(0, 6) + it.substring(6, 10)
                    '5' ->
                        fri = if (fri.isEmpty()) it
                        else fri.substring(0, 6) + it.substring(6, 10)
                    else -> mon = ""
                }
            }
            val daysList = listOf(mon, tue, wed, thu, fri).filter { it.isNotEmpty() }

            newItem.time = daysList.joinToString(", ")
        }

        Log.d("currentList in addNewCourseToTimetable", "$currentList")

        // 리스트에 새로운 강의 추가
        currentList.add(newItem)

        // 수정된 리스트를 다시 LiveData에 설정
        _userCourseList.postValue(currentList)
    }

    fun initTimetable() {
        _userCourseList.postValue(mutableListOf())
    }

    fun setExampleTimetable() {
        initTimetable()
        addNewCourseToTimetable(Course("1234", "임시 과목", "교수명", "1_08000900, 5_08000900", "27312", "온라인", "2", 2023))
        addNewCourseToTimetable(Course("1235", "임시 과목", "교수명", "2_08000900", "21312", "온라인", "2", 2023))
        val size = userCourseList.value?.size
        Log.d("Function", "setExampleTimetable $size")
    }

    fun enableStartPoint() {
        _isSelectingStartPoint.value = true
    }

    fun disableStartPoint() {
        _isSelectingStartPoint.value = false
    }

    fun setStartPoint(input: String) {
        _startPoint.value = input
    }
}
package edu.skku.cs.skkedula.fragments.timetable

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import edu.skku.cs.skkedula.api.CourseInfo

class TimetableViewModel : ViewModel() {
    private val _tempCourseName = MutableLiveData<String>("")
    val tempCourseName: LiveData<String> = _tempCourseName

    private val _tempClassroom = MutableLiveData<String>("")
    val tempClassroom: LiveData<String> = _tempClassroom

    private val _startPoint = MutableLiveData<String>("시간표에서 출발지 선택하기(기본: 현위치)")
    val startPoint: LiveData<String> = _startPoint

    private val _userCourseList = MutableLiveData<List<CourseInfo>>(emptyList())
    val userCourseList: LiveData<List<CourseInfo>> = _userCourseList

    fun setTempCourseInfo(name: String, classroom: String) {
        _tempCourseName.value = name
        _tempClassroom.value = classroom
    }

    fun addNewCourse(newItem: CourseInfo) {
        // 현재의 리스트를 가져와서 수정
        val currentList = _userCourseList.value?.toMutableList() ?: mutableListOf()
        currentList.add(newItem)

        // 수정된 리스트를 다시 LiveData에 설정
        _userCourseList.value = currentList
    }

}
package edu.skku.cs.skkedula.fragments.timetable

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData

class TimetableViewModel : ViewModel() {
    private val _tempCourseName = MutableLiveData<String>("")
    val tempCourseName: LiveData<String> = _tempCourseName

    private val _tempClassroom = MutableLiveData<String>("")
    val tempClassroom: LiveData<String> = _tempClassroom

    private val _startPoint = MutableLiveData<String>("시간표에서 출발지 선택하기(기본: 현위치)")
    val startPoint: LiveData<String> = _startPoint

    fun setTempCourseInfo(name: String, classroom: String) {
        _tempCourseName.value = name
        _tempClassroom.value = classroom
    }
}
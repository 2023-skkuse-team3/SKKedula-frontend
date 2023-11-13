package edu.skku.cs.skkedula.API

import com.google.gson.annotations.SerializedName
data class Course(
    @SerializedName("course_id")
    val courseId: String,
    @SerializedName("course_name")
    val courseName: String,
    val professor: String,
    @SerializedName("time_start")
    val timeStart: String,
    @SerializedName("time_total")
    val timeTotal: Int,
    @SerializedName("room_num")
    val roomNum: Int,
    val semester: String,
    val year: Int
)

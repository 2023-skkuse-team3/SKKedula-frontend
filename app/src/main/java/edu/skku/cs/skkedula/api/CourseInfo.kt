package edu.skku.cs.skkedula.api

import com.google.gson.annotations.SerializedName

data class CourseInfo(
    @SerializedName("course_name")
    val courseName: String,
    val professor: String,
    @SerializedName("start_time")
    val startTime: List<String>,
    @SerializedName("end_time")
    val endTime: String,
    @SerializedName("day_of_week")
    val dayOfWeek: List<String>,
    @SerializedName("building_num")
    val buildingNum: Int,
)
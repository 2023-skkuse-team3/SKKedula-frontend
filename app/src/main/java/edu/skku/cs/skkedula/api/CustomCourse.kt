package edu.skku.cs.skkedula.api

import com.google.gson.annotations.SerializedName

data class CustomCourse(
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("course_name")
    val courseName: String,
    @SerializedName("Time")
    val time: String,
    @SerializedName("Room_num")
    val roomNum: String
    )

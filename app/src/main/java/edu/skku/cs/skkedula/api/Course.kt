package edu.skku.cs.skkedula.api

import com.google.gson.annotations.SerializedName
data class Course(
    @SerializedName("Course_ID")
    val courseId: String,
    @SerializedName("Course_name")
    val courseName: String,
    @SerializedName("Professor")
    val professor: String,
    @SerializedName("Time")
    val time: String,
    @SerializedName("Room_num")
    val roomNum: String,
    @SerializedName("Class_type")
    val classType: String,
    @SerializedName("Semester")
    val semester: String,
    @SerializedName("Year")
    val year: Int
)

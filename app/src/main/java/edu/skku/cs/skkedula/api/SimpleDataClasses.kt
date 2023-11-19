package edu.skku.cs.skkedula.api

import androidx.lifecycle.LiveData
import com.google.gson.annotations.SerializedName

// 강의 검색을 위한 data class
data class CourseName(@SerializedName("course_name") val courseName: String)
data class Professor(@SerializedName("professor") val professor: String)
data class UserId(@SerializedName("user_id") val userId: String)
data class UserCourse(@SerializedName("user_id") val userId: String, @SerializedName("course_id") val courseId: String)
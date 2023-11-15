package edu.skku.cs.skkedula.api

import com.google.gson.annotations.SerializedName

data class AddCourseBody(
    @SerializedName("course_ID")
    val courseId: String,
    @SerializedName("userID")
    val userId: String
)

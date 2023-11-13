package edu.skku.cs.skkedula.API

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitService {
    @GET("/timetable/search/")
    fun searchCourse(@Query("searchQuery") searchQuery: String) : Call<List<Course>>

    @GET("/{userId}/timetables/courses")
    fun getUserCourses(@Path("userId") userId: String): Call<List<Course>>
}
package edu.skku.cs.skkedula.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitService {
    @GET("/timetable/search/")
    fun searchCourse(@Query("searchQuery") searchQuery: String) : Call<List<Course>>

    @GET("/{userId}/timetables/courses")
    fun getUserCourses(@Path("userId") userId: String): Call<List<Course>>

    // URL 보낸 후 유저 강의 정보 가져오기
    @POST("/{userId}/timetables/courses")
    fun postUserCourses(@Path("userId") userId: String): Call<List<CourseInfo>>
}
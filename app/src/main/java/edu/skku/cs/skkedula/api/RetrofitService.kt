package edu.skku.cs.skkedula.api

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

data class CourseName(@SerializedName("course_name") val courseName: String)

interface RetrofitService {
    @POST("/timetable/searchByCourse")
    fun searchCourse(@Body searchQuery: CourseName) : Call<List<Course>>

    @POST("/timetable/searchByProfessor/")
    fun searchCourseByProfessor(@Query("searchQuery") searchQuery: String) : Call<List<Course>>

    @GET("/{userId}/timetables/courses")
    fun getUserCourses(@Path("userId") userId: String): Call<List<Course>>

    // URL 보낸 후 유저 강의 정보 가져오기
    @POST("/{userId}/timetables/courses")
    fun postUserCourses(@Path("userId") userId: String): Call<List<CourseInfo>>

    @GET("studyspaces")
    fun getstudyspaces(@Path("userId") userId: String): Call<List<Studyspaces>>

    @GET("buildings")
    fun getbuildings(@Path("userId") userId: String): Call<List<Buildings>>

    @POST("searchBybuildings")
    fun searchbuildings(@Query("searchQuery") searchQuery: String) : Call<List<Buildings>>
}
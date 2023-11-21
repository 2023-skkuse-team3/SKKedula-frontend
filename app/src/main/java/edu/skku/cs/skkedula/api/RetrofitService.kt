package edu.skku.cs.skkedula.api

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


data class Message(val message: String)

interface RetrofitService {
    @POST("/register")
    fun registerUser(@Body registration: Registration): Call<RegistrationResponse>

    @POST("/login")
    fun loginUser(@Body login: Login): Call<LoginResponse>

    @POST("/timetable/searchByCourse")
    fun searchCourse(@Body searchQuery: CourseName) : Call<List<Course>>

    @POST("/timetable/searchByProfessor/")
    fun searchCourseByProfessor(@Body searchQuery: Professor) : Call<List<Course>>

    @GET("/timetables/courses")
    fun getUserCourses(@Body data: UserId): Call<List<Course>>

    @POST("/custom-courses")
    fun addCustomCourse(@Body data: CustomCourse): Call<Message>

    @POST("/reset-timetable")
    fun resetTimetable(@Body data: UserId) : Call<Message>

    // URL 보낸 후 유저 강의 정보 가져오기
    @POST("/scrape_course_info")
    fun postUserCourses(@Body data: Url): Call<UrlResponse>

    @POST("/timetables/delete-course")
    fun removeCourseFromTimetable(@Body body: UserCourse) : Call<Message>

    @POST("timetable/addSelectedCourse")
    fun addCourseToTimetable(@Body body: UserCourse) : Call<Message>

    @POST("/search/building")
    fun searchBuilding(@Body body: Building): Call<BuildingResponse>

    @GET("/studyspaces")
    fun getStudyspace(): Call<List<Studyspace>>

    @POST("/savepath")
    fun savePath(@Body savepath: Savepath): Call<SavepathResponse>

    @POST("/getpath")
    fun getPath(@Body getpath: Getpath): Call<GetpathResponse>

    @HTTP(method="DELETE", path = "/deletepath", hasBody = true)
    fun deletePath(@Body deletepath: Deletepath): Call<DeletepathResponse>
}
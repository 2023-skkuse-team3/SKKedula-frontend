package edu.skku.cs.skkedula.api

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

// 강의 검색을 위한 data class
data class CourseName(@SerializedName("course_name") val courseName: String)
data class Professor(@SerializedName("professor") val professor: String)

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

    @GET("/{userId}/timetables/courses")
    fun getUserCourses(@Path("userId") userId: String): Call<List<Course>>

    // URL 보낸 후 유저 강의 정보 가져오기
    @POST("/{userId}/timetables/courses")
    fun postUserCourses(@Path("userId") userId: String): Call<List<Course>>

    @POST("timetable/addSelectedCourse")
    fun addCourseToTimetable(@Body body: AddCourseBody) : Call<Message>

    //@POST("/add_study_space")
    //fun poststudyspaces(@Body poststudyspaces: Studyspaces) : Call<List<Studyspaces>>

    //@POST("buildings")
    //fun postbuildings(@Body postbuildings: Buildings): Call<List<Buildings>>

    //@GET("/study_spaces_in_building")
    //fun searchstudyspace(@Body postbuildings: Buildings): Call<List<Buildings>>

    //@POST("searchBybuildings")
    //fun searchbuildings(@Query("searchQuery") searchQuery: String) : Call<List<Buildings>>

    @POST("/savepath")
    fun savePath(@Body savepath: Savepath): Call<SavepathResponse>

    @POST("/getpath")
    fun getPath(@Body getpath: Getpath): Call<GetpathResponse>

    @HTTP(method="DELETE", path = "/deletepath", hasBody = true)
    fun deletePath(@Body deletepath: Deletepath): Call<DeletepathResponse>
}
package edu.skku.cs.skkedula.api


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// 8000포트 사용을 위한 object. url 등록 시에만 사용함.
object ApiObject8000 {
    private const val URL = "http://3.35.230.23:8000/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: RetrofitService = retrofit.create(RetrofitService::class.java)
}
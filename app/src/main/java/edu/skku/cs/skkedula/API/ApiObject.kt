package edu.skku.cs.skkedula.API


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiObject {
    private const val URL = "http://3.35.47.201:3000"

    private val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: RetrofitService = retrofit.create(RetrofitService::class.java)
}
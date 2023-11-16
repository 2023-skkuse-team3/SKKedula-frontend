package edu.skku.cs.skkedula.api
import com.google.gson.annotations.SerializedName

data class Login(
    @SerializedName("user_id") val user_id: String,
    @SerializedName("password") val password: String
)

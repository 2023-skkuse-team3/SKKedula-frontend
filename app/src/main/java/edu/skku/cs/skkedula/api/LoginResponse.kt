package edu.skku.cs.skkedula.api
import com.google.gson.annotations.SerializedName

data class LoginResponse (
    @SerializedName("message") val message: String
)
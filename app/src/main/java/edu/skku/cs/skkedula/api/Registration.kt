package edu.skku.cs.skkedula.api
import com.google.gson.annotations.SerializedName

data class Registration(
    @SerializedName("user_id") val user_id: String,
    @SerializedName("name") val name: String,
    @SerializedName("password") val password: String,
    @SerializedName("confirmPassword") val confirmPassword: String
)

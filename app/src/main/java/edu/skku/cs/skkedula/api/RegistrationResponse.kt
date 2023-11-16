package edu.skku.cs.skkedula.api
import com.google.gson.annotations.SerializedName

data class RegistrationResponse (
    @SerializedName("message") val message: String
)
package edu.skku.cs.skkedula.api

import com.google.gson.annotations.SerializedName

data class SavepathResponse(
    @SerializedName("message") val message: String
)

package edu.skku.cs.skkedula.api

import com.google.gson.annotations.SerializedName

data class Getpath(
    @SerializedName("ID") val ID: String,
    @SerializedName("Sequence") val Sequence: Int
)

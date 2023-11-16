package edu.skku.cs.skkedula.api

import com.google.gson.annotations.SerializedName

data class Deletepath(
    @SerializedName("ID") val ID: String,
    @SerializedName("Sequence") val Sequence: Int
)

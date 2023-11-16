package edu.skku.cs.skkedula.api

import com.google.gson.annotations.SerializedName

data class Savepath(
    @SerializedName("ID") val ID: String,
    @SerializedName("Sequence") val Sequence: Int,
    @SerializedName("Stopover_count") val Stopover_count: Int,
    @SerializedName("Start_latitude") val Start_latitude: Double,
    @SerializedName("Start_longitude") val Start_longitude: Double,
    @SerializedName("End_latitude") val End_latitude: Double,
    @SerializedName("End_longitude") val End_longitude: Double,
    @SerializedName("Stopover") val Stopover: String
)

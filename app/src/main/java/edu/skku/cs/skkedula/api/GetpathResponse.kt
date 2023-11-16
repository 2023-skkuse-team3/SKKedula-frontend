package edu.skku.cs.skkedula.api

import com.google.gson.annotations.SerializedName

data class GetpathResponse(
    @SerializedName("ID") val ID: String,
    @SerializedName("Sequence") val Sequence: Int,
    @SerializedName("Coordinate_count") val Coordinate_count: Int,
    @SerializedName("Start_latitude") val Start_latitude: String,
    @SerializedName("Start_longitude") val Start_longitude: String,
    @SerializedName("End_latitude") val End_latitude: String,
    @SerializedName("End_longitude") val End_longitude: String,
    @SerializedName("Stopover") val Stopover: String
)

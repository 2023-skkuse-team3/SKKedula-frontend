package edu.skku.cs.skkedula.api

import com.google.gson.annotations.SerializedName

data class Studyspace(
    @SerializedName("Studyspace_ID")
    val studyspaceID: String,
    @SerializedName("Name")
    val name: String,
    @SerializedName("Building_num")
    val buildingNum: String,
    @SerializedName("Latitude")
    val latitude: Double,
    @SerializedName("Longitude")
    val longitude: Double,
    @SerializedName("Floor")
    val floor: Int
)

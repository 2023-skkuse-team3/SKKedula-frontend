package edu.skku.cs.skkedula.api

import com.google.gson.annotations.SerializedName
data class Studyspaces(
    @SerializedName("Studyspace_ID")
    val studyspaceID: Int,
    @SerializedName("Name")
    val name: String,
    @SerializedName("Building_num")
    val buildingNum: Int,
    @SerializedName("Latitude")
    val latitude: Double,
    @SerializedName("Longitude")
    val longitude: Double,
    @SerializedName("Floor")
    val floor: Int,
)

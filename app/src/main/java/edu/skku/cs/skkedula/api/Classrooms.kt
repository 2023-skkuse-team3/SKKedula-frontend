package edu.skku.cs.skkedula.api

import com.google.gson.annotations.SerializedName
data class Classrooms (
    @SerializedName("Room_num")
    val RoomNum: Int,
    @SerializedName("Building_num")
    val buildingNum: Int,
    @SerializedName("Latitude")
    val latitude: Double,
    @SerializedName("Longitude")
    val longitude: Double,
)
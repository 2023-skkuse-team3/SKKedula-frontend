package edu.skku.cs.skkedula.api

import com.google.gson.annotations.SerializedName
data class Buildings (
    @SerializedName("Building_num")
    val buildingNum: Int,
    @SerializedName("Building_name")
    val buildingName: String,
    @SerializedName("Latitude")
    val latitude: Double,
    @SerializedName("Longitude")
    val longitude: Double,
    @SerializedName("Study_space")
    val studySpace: Int,
    @SerializedName("Entrance_count")
    val entranceCount: Int,
    @SerializedName("E_Latitudes")
    val eLatitude: List<Double>,
    @SerializedName("E_Longitudes")
    val eLongitude: List<Double>,
)
package edu.skku.cs.skkedula.api

import com.google.gson.annotations.SerializedName

data class BuildingResponse(
    @SerializedName("Building_Name")
    val buildingName: String,
    @SerializedName("Latitude")
    val latitude: Double,
    @SerializedName("Longitude")
    val longitude: Double
)

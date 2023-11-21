package edu.skku.cs.skkedula.api

import com.google.gson.annotations.SerializedName

data class BuildingResponse(
    @SerializedName("Building_num")
    val buildingName: String = "", // nullable 제거
    @SerializedName("Latitude")
    val latitude: Double = 0.0, // Double로 변경
    @SerializedName("Longitude")
    val longitude: Double = 0.0 // Double로 변경
)

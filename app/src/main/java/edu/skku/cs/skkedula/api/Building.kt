package edu.skku.cs.skkedula.api

import com.google.gson.annotations.SerializedName

data class Building(
    @SerializedName("Building_Num")
    var buildingNum: String? = null,
    @SerializedName("Building_Name")
    val buildingName: String? = null,
    @SerializedName("Room_Num")
    val roomNum: String? = null
) {
    companion object {
        fun fromName(name: String) = Building(buildingName = name)
        fun fromBuildingNum(num: Int) = Building(buildingNum = num.toString())
        fun fromRoomNum(num: Int) = Building(roomNum = num.toString())
    }
}

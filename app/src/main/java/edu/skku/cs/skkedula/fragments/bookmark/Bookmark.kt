package edu.skku.cs.skkedula.fragments.bookmark
import com.naver.maps.geometry.LatLng

class Bookmark (
    val start: String,
    val end: String,
    val startlatlng: LatLng,
    val endlatlng: LatLng,
    val stopover: MutableList<LatLng>
)
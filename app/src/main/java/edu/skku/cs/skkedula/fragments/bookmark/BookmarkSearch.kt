package edu.skku.cs.skkedula.fragments.bookmark

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.naver.maps.geometry.LatLng

class BookmarkSearch: ViewModel() {
    val item = Bookmark(
        start = "26310",
        end = "23217",
        startlatlng = LatLng(37.29485663219833, 126.97587565874039),
        endlatlng = LatLng(37.294301, 126.976732),
        stopover = mutableListOf(LatLng(37.29485663219833, 126.97587565874039), LatLng(37.29412794877511, 126.97576437025737))
    )
}
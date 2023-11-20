package edu.skku.cs.skkedula.fragments.map


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapViewModel : ViewModel() {
    val startText = MutableLiveData<String>()
    val endText = MutableLiveData<String>()
    val startLocation = MutableLiveData<String>()
    val endLocation = MutableLiveData<String>()

    // Add a new LiveData for marker click
    /*private val _markerClick = MutableLiveData<Study?>()
    val markerClicked: LiveData<Study?> = _markerClick

    fun onMarkerClicked(entrance: Study?) {
        _markerClick.value = entrance
    }*/
}
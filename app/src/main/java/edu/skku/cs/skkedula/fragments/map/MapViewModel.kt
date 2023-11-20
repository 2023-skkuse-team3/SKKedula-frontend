package edu.skku.cs.skkedula.fragments.map


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.skku.cs.skkedula.api.BuildingResponse
import edu.skku.cs.skkedula.api.Studyspace

class MapViewModel : ViewModel() {
    val startText = MutableLiveData<String>()
    val endText = MutableLiveData<String>()
    val startLocation = MutableLiveData<String>()
    val endLocation = MutableLiveData<String>()

    // Add a new LiveData for marker click
    private val _markerClick = MutableLiveData<Studyspace?>()
    val markerClicked: LiveData<Studyspace?> = _markerClick

    fun onMarkerClicked(studyspace: Studyspace?) {
        _markerClick.value = studyspace
    }

    // 새로운 LiveData 추가
    private val _buildingData = MutableLiveData<BuildingResponse>()
    val buildingData: LiveData<BuildingResponse> = _buildingData

    fun onBuildingDataReceived(building: BuildingResponse) {
        _buildingData.value = building
    }
}
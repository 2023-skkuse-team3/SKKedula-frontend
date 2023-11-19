package edu.skku.cs.skkedula.fragments.map


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapViewModel : ViewModel() {
    val startText = MutableLiveData<String>()
    val endText = MutableLiveData<String>()
    val startLocation = MutableLiveData<String>()
    val endLocation = MutableLiveData<String>()
}
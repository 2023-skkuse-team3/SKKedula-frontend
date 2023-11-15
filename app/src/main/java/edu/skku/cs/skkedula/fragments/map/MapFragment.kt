package edu.skku.cs.skkedula.fragments.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentContainerView
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.util.FusedLocationSource
import edu.skku.cs.skkedula.R
import edu.skku.cs.skkedula.databinding.FragmentMapBinding
import com.naver.maps.map.MapFragment
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import edu.skku.cs.skkedula.EntranceDetailFragment
import edu.skku.cs.skkedula.StudyDetailFragment
import edu.skku.cs.skkedula.api.ApiObject
import edu.skku.cs.skkedula.api.Buildings
import edu.skku.cs.skkedula.api.Studyspaces
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource

    private val LOCATION_PERMISSION_REQUEST_CODE = 5000
    private val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    // ActivityResultLauncher 선언
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ActivityResultLauncher 초기화
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all { it.value }
            if (granted) {
                initMapView()
            }
            // 여기에 권한 거부에 대한 처리 로직 추가 (옵션)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val buttonNavigate = binding.route
        buttonNavigate.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment_activity_main, RoutesearchFragment())
            transaction.commit()
        }

        val buttonStudy = binding.study
        buttonStudy.setOnClickListener {
            loadStudy("userId")
        }

        val buttonEntrance = binding.entrance
        buttonEntrance.setOnClickListener {
            loadEntrance("userId")
        }

        val searchEditText = binding.search
        searchEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(v.text.toString())
                true
            } else false
        }

        // 위치 권한 확인 및 요청
        if (!hasPermission()) {
            permissionLauncher.launch(PERMISSIONS)
        } else {
            initMapView()
        }

        return binding.root
    }

    private fun initMapView() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                childFragmentManager.beginTransaction().add(R.id.map, it).commit()
            }
        mapFragment.getMapAsync(this)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    private fun hasPermission(): Boolean {
        return PERMISSIONS.all {
            ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        naverMap.locationSource = locationSource
        naverMap.uiSettings.isLocationButtonEnabled = true
        naverMap.locationTrackingMode = LocationTrackingMode.Follow
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadEntrance(userId: String) {
        ApiObject.service.getbuildings(userId).enqueue(object : Callback<List<Buildings>> {
            override fun onResponse(call: Call<List<Buildings>>, response: Response<List<Buildings>>) {
                if (response.isSuccessful) {
                    val buildings = response.body()
                    buildings?.forEach { building ->
                        for (i in 0 until building.entranceCount) {
                            val latitude = building.eLatitude.getOrNull(i)
                            val longitude = building.eLongitude.getOrNull(i)
                            if (latitude != null && longitude != null) {
                                addMarker(latitude, longitude, "Entrance ${i + 1} of Building", R.drawable.entrance_marker, 0, entranceName = "temp")
                            }
                        }
                    }
                } else {
                    // Handle unsuccessful response
                    Log.e("MapFragment", "Error fetching buildings: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<List<Buildings>>, t: Throwable) {
                Log.e("MapFragment", "Failed to fetch buildings", t)
            }
        })
    }

    private fun loadStudy(userId: String) {
        ApiObject.service.getstudyspaces(userId).enqueue(object : Callback<List<Studyspaces>> {
            override fun onResponse(call: Call<List<Studyspaces>>, response: Response<List<Studyspaces>>) {
                if (response.isSuccessful) {
                    val studySpaces = response.body()
                    studySpaces?.forEach { space ->
                        addMarker(space.latitude, space.longitude, "${space.name}, Building ${space.buildingNum}, Floor ${space.floor}", R.drawable.study_marker, 2, entranceName = "temp")
                    }
                } else {
                    Log.e("MapFragment", "Error: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<List<Studyspaces>>, t: Throwable) {
                Log.e("MapFragment", "Failed to fetch study spaces", t)
            }
        })
    }

    private fun performSearch(query: String) {
        // Assuming ApiObject.service has a method to search buildings by name or number
        ApiObject.service.searchbuildings(query).enqueue(object : Callback<List<Buildings>> {
            override fun onResponse(call: Call<List<Buildings>>, response: Response<List<Buildings>>) {
                if (response.isSuccessful) {
                    val buildings = response.body()
                    buildings?.firstOrNull()?.let { building ->
                        addMarker(building.latitude, building.longitude, building.buildingName, R.drawable.icon_startpoint, 1, entranceName = "temp")
                    }
                } else {
                    // Handle error
                    Log.e("MapFragment", "Error: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<List<Buildings>>, t: Throwable) {
                // Handle failure
            }
        })
    }

    private fun addMarker(latitude: Double, longitude: Double, caption: String, markerIcon: Int, markerType: Int, entranceName: String) {
        val position = LatLng(latitude, longitude)
        val marker = Marker()
        marker.position = position
        marker.map = naverMap
        marker.captionText = caption
        marker.icon = OverlayImage.fromResource(markerIcon)
        marker.tag = markerType  // Set a tag to identify markers

        marker.setOnClickListener {
            when (marker.tag) {
                1 -> showSearchResultCard(entranceName)
                2 -> showStudySpaceCard(entranceName)
                // No action for 0 (loadEntrance markers)
            }
            true
        }
    }

    private fun showSearchResultCard(entranceName: String) {
        val entranceDetailFragment = EntranceDetailFragment.newInstance(entranceName)
        val transaction = parentFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.bottom_up, 0, 0, R.anim.bottom_down) // Animation for overlay
        transaction.add(requireView().findViewById(R.id.card), entranceDetailFragment) // Add the fragment to the card container
        transaction.addToBackStack(null) // Optionally, add to back stack
        transaction.commit()
        requireView().findViewById<View>(R.id.card).visibility = View.VISIBLE // Make the card container visible
    }

    private fun showStudySpaceCard(entranceName: String) {
        val studyDetailFragment = StudyDetailFragment.newInstance(entranceName)
        val transaction = parentFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.bottom_up, 0, 0, R.anim.bottom_down) // Animation for overlay
        transaction.add(requireView().findViewById(R.id.card), studyDetailFragment) // Add the fragment to the card container
        transaction.addToBackStack(null) // Optionally, add to back stack
        transaction.commit()
        requireView().findViewById<View>(R.id.card).visibility = View.VISIBLE // Make the card container visible
    }

}

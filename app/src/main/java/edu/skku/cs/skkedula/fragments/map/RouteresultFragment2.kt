package edu.skku.cs.skkedula.fragments.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.util.FusedLocationSource
import edu.skku.cs.skkedula.R
import edu.skku.cs.skkedula.databinding.FragmentRouteresult2Binding
import edu.skku.cs.skkedula.fragments.bookmark.BookmarkSearch
import edu.skku.cs.skkedula.fragments.bookmark.BookmarkViewModel
import kotlin.math.pow

class RouteresultFragment2 : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentRouteresult2Binding?=null
    private val binding get() = _binding!!
    //private val mapViewModel: MapViewModel by activityViewModels()
    private lateinit var mapViewModel: MapViewModel

    private lateinit var startString: String
    private lateinit var endString: String
    private lateinit var endpos: LatLng
    private lateinit var startpos: LatLng
    //private var stopoverpoint = mapViewModel.stopover
    private lateinit var stopoverpoint: MutableList<LatLng>
    private var waypoints: MutableList<LatLng> = mutableListOf()

    private lateinit var startMarker: Marker
    private lateinit var endMarker: Marker
    private val markers = mutableListOf<Marker>()
    var avglat = 0.0
    var avglng = 0.0

    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private val locationPermissionRequestCode = 5000
    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    private fun initMapView() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map3) as MapFragment?
                ?: MapFragment.newInstance().also {
                    childFragmentManager.beginTransaction()
                        .add(R.id.map, it).commit()
                }
        mapFragment.getMapAsync(this)
        locationSource = FusedLocationSource(this, locationPermissionRequestCode)
    }

    private fun hasPermission(): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(
                requireContext(),
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        naverMap.locationSource = locationSource
        naverMap.uiSettings.isLocationButtonEnabled = true
        naverMap.locationTrackingMode = LocationTrackingMode.Follow
        mapViewModel = ViewModelProvider(requireActivity()).get(MapViewModel::class.java)
        stopoverpoint = mapViewModel.stopover
        startString = mapViewModel.startText.toString()
        endString = mapViewModel.endText.toString()
        endpos = mapViewModel.startCoordinate
        startpos = mapViewModel.endCoordinate

        startMarker = Marker().apply {
            icon = OverlayImage.fromResource(R.drawable.icon_endpoint)
        }
        startMarker.position = startpos
        startMarker.map = naverMap

        endMarker = Marker().apply {
            icon = OverlayImage.fromResource(R.drawable.icon_startpoint)
        }
        endMarker.position = endpos
        endMarker.map = naverMap

        waypoints.add(startMarker.position as LatLng)
        stopoverpoint.reversed().forEach { point ->
            waypoints.add(point)
            val waypointMarker = Marker()
            waypointMarker.position = point
            waypointMarker.icon = OverlayImage.fromResource(R.drawable.study_marker)
            waypointMarker.map = naverMap
        }
        waypoints.add(endMarker.position as LatLng)
        val pathOverlay = PathOverlay()
        pathOverlay.coords = waypoints
        pathOverlay.map = naverMap
        var timeview = binding.timeTextView
        var distview = binding.distanceTextView
        val distance = calculateDistance(waypoints)
        val time = calculateEstimatedTime(distance)
        timeview.text = time.toString() + "분"
        distview.text = distance.toString() + "m"

        avglat = (startMarker.position.latitude + endMarker.position.latitude)
        avglng = (startMarker.position.longitude + endMarker.position.longitude)

        val initialCameraPosition = CameraPosition(LatLng(avglat/2, avglng/2), 16.2)
        naverMap.moveCamera(CameraUpdate.toCameraPosition(initialCameraPosition))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ActivityResultLauncher 초기화
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                val granted = permissions.entries.all { it.value }
                if (granted) {
                    initMapView()
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRouteresult2Binding.inflate(inflater, container, false)
        val rootView = binding.root
        rootView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        mapViewModel = ViewModelProvider(requireActivity()).get(MapViewModel::class.java)

        // Way control area
        if (!hasPermission()) {
            permissionLauncher.launch(permissions)
        } else {
            initMapView()
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapViewModel = ViewModelProvider(requireActivity()).get(MapViewModel::class.java)
        startString = mapViewModel.startText.value.toString()
        endString = mapViewModel.endText.value.toString()
        val editstart = view.findViewById<TextView>(R.id.startText)
        editstart.text = startString
        val editend = view.findViewById<TextView>(R.id.endText)
        editend.text = endString
    }

    private fun calculateDistance(waypoints: List<LatLng>): Double {
        var distance = 0.0
        for (i in 0 until waypoints.size - 1) {
            distance += calculateDistance(waypoints[i], waypoints[i + 1])
        }
        return Math.round(distance * 10.0) / 10.0
    }

    private fun calculateDistance(start: LatLng, end: LatLng): Double {
        val earthRadius = 6371.0

        val lat1 = Math.toRadians(start.latitude)
        val lon1 = Math.toRadians(start.longitude)
        val lat2 = Math.toRadians(end.latitude)
        val lon2 = Math.toRadians(end.longitude)

        val dLat = lat2 - lat1
        val dLon = lon2 - lon1

        val a = Math.sin(dLat / 2).pow(2) + Math.cos(lat1) * Math.cos(lat2) * Math.sin(dLon / 2).pow(2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        val distanceInKm = earthRadius * c
        val distanceInMeters = distanceInKm * 1000
        return Math.round(distanceInMeters * 10.0) / 10.0
    }

    private fun calculateEstimatedTime(distanceInMeters: Double): Double {
        val averageWalkingSpeed = 1.0
        val estimatedTimeInSeconds = distanceInMeters / averageWalkingSpeed

        val estimatedTimeInMinutes = estimatedTimeInSeconds / 60.0

        return if (estimatedTimeInMinutes <= 0) {
            1.0
        } else {
            Math.round(estimatedTimeInMinutes * 10.0) / 10.0
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
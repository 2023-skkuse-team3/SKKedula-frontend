package edu.skku.cs.skkedula.fragments.map

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import edu.skku.cs.skkedula.LoginActivity
import edu.skku.cs.skkedula.R
import edu.skku.cs.skkedula.api.ApiObject
import edu.skku.cs.skkedula.api.Building
import edu.skku.cs.skkedula.api.BuildingResponse
import edu.skku.cs.skkedula.api.Deletepath
import edu.skku.cs.skkedula.api.DeletepathResponse
import edu.skku.cs.skkedula.api.Savepath
import edu.skku.cs.skkedula.api.SavepathResponse
import edu.skku.cs.skkedula.databinding.FragmentRouteresultBinding
import edu.skku.cs.skkedula.fragments.bookmark.Bookmark
import edu.skku.cs.skkedula.fragments.bookmark.BookmarkViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.pow

class RouteresultFragment : Fragment(), OnMapReadyCallback {
    private lateinit var _binding: FragmentRouteresultBinding
    private val binding get() = _binding!!
    private val mapViewModel: MapViewModel by activityViewModels()
    private var startString: String = " "
    private var endString: String = " "
    private var stopoverpoint: MutableList<LatLng> = mutableListOf()
    private var stopovermarker: MutableList<Marker> = mutableListOf()
    private var waypoints: MutableList<LatLng> = mutableListOf()
    private var markersVisible = false
    private var startMarker: Marker?=null
    private var endMarker: Marker?=null
    private val markers = mutableListOf<Marker>()
    var avglat = 0.0
    var avglng = 0.0
    private val bookmarkViewModel: BookmarkViewModel by activityViewModels()


    private fun applyDarkEffect(bookmarkbutton:ImageView) {
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f)
        val colorFilter = ColorMatrixColorFilter(colorMatrix)
        bookmarkbutton.colorFilter = colorFilter
    }
    private fun removeDarkEffect(bookmarkbutton:ImageView) {
        bookmarkbutton.colorFilter = null
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private val entrances = listOf(
        Entrance("의학관", 72, 1, Point(37.29234631, 126.9731447)),
        Entrance("의학관", 72, 2, Point(37.29198142, 126.9732886)),
        Entrance("생명공학관", 61, 1, Point(37.29578166, 126.9737695)),
        Entrance("생명공학관", 61, 2, Point(37.29601151, 126.9741839)),
        Entrance("생명공학관", 62, 1, Point(37.29603631, 126.9742572)),
        Entrance("생명공학관", 62, 2, Point(37.2960635, 126.9750184)),
        Entrance("기초학문관", 51, 1, Point(37.29529969, 126.9741587)),
        Entrance("기초학문관", 51, 2, Point(37.29557912, 126.9746605)),
        Entrance("제2과학관", 32, 1, Point(37.29482673, 126.9745705)),
        Entrance("제2과학관", 32, 2, Point(37.29514673, 126.9751597)),
        Entrance("제2과학관", 32, 3, Point(37.29508379, 126.9757941)),
        Entrance("제1과학관", 31, 1, Point(37.2944618, 126.9745227)),
        Entrance("제1과학관", 31, 2, Point(37.29432223, 126.9749457)),
        Entrance("제1과학관", 31, 3, Point(37.29467375, 126.9754728)),
        Entrance("약학관", 53, 1, Point(37.29204746, 126.9766522)),
        Entrance("제1공학관", 21, 1, Point(37.29375485, 126.9762485)),
        Entrance("제1공학관", 21, 2, Point(37.29359716, 126.9762006)),
        Entrance("제1공학관", 22, 1, Point(37.29384735, 126.9769872)),
        Entrance("제1공학관", 23, 1, Point(37.29424825, 126.976691)),
        Entrance("제1공학관", 23, 2, Point(37.29419631, 126.9760341)),
        Entrance("제2공학관", 25, 1, Point(37.29484745, 126.9767388)),
        Entrance("제2공학관", 26, 1, Point(37.29514491, 126.977342)),
        Entrance("제2공학관", 27, 1, Point(37.2955007, 126.9766906)),
        Entrance("제2공학관", 27, 2, Point(37.29539024, 126.9763016)),
        Entrance("산학협력센터", 85, 1, Point(37.29610871, 126.9757656)),
        Entrance("산학협력센터", 85, 2, Point(37.29594652, 126.9757628)),
        Entrance("산학협력센터", 85, 3, Point(37.29589025, 126.9759912)),
        Entrance("삼성학술정보관", 48, 1, Point(37.29422312, 126.9749711)),
        Entrance("삼성학술정보관", 48, 2, Point(37.29365094, 126.9748726)),
        Entrance("학생회관", 3, 1, Point(37.29405388, 126.9736009)),
        Entrance("수성관", 5, 1, Point(37.29334865, 126.9728624)),
        Entrance("체육관", 72, 1, Point(37.29274, 126.9709454)),
        Entrance("대강당", 70, 1, Point(37.29257816, 126.9724144)),
        Entrance("기숙사신관", 98, 1, Point(37.29649081, 126.9719477)),
        Entrance("기숙사신관", 98, 2, Point(37.29626583, 126.9731462)),
        Entrance("기숙사인관", 91, 1, Point(37.29681111, 126.973848)),
        Entrance("기숙사의관", 92, 1, Point(37.29689237, 126.9746093)),
        Entrance("기숙사예관", 93, 1, Point(37.29652314, 126.9755399)),
        Entrance("기숙사지관", 95, 1, Point(37.29639513, 126.9774742)),
        Entrance("화학관", 33, 1, Point(37.29170072, 126.9774812)),
        Entrance("화학관", 33, 2, Point(37.29156318, 126.9768074)),
        Entrance("반도체관", 40, 1, Point(37.29173679, 126.977594)),
        Entrance("반도체관", 40, 2, Point(37.29158363, 126.9776927)),
        Entrance("N센터", 86, 1, Point(37.29217794, 126.9757781)),
        Entrance("복지회관", 4, 1, Point(37.29398384, 126.97265356622))
    )

    private fun addEntranceMarkersToMap() {
        entrances.forEach { entrance ->
            val marker = Marker().apply {
                position = LatLng(entrance.location.x, entrance.location.y)
                icon = OverlayImage.fromResource(R.drawable.entrance_marker)
                map = naverMap
            }
            markers.add(marker) // Add the marker to the list
        }
    }

    private fun removeMarkersFromMap() {
        markers.forEach { it.map = null } // Remove each marker from the map
        markers.clear() // Clear the list of markers
    }

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

    // return -1 if 현위치, return 0 if exact location not in db, else return 1
    private fun positionParser(str: String): Int {
        if (str.length < 3) return -2
        if (str.equals("현위치")) return -1
        else if (str.substring(0, 2) == "27" || str.substring(0, 2) == "26") {
            return 1
        }
        else if (str.substring(0, 2) == "도서") return 2
        else return 0
    }

    override fun onMapReady(naverMap: NaverMap) {
        mapViewModel.startLocation.observe(viewLifecycleOwner) { text ->
            startString = text
        }
        mapViewModel.endLocation.observe(viewLifecycleOwner) { text ->
            endString = text
        }
        if (startString.length == 1) startString = "현위치"
        if (endString.length == 1) endString = "도서관 스터디룸"
        Log.d("DATA", "-----${startString} ---- ${endString}------")
        this.naverMap = naverMap
        naverMap.locationSource = locationSource
        naverMap.uiSettings.isLocationButtonEnabled = true
        naverMap.locationTrackingMode = LocationTrackingMode.Follow

        naverMap.setOnMapClickListener { _, coord ->
            val waypointMarker = Marker()
            Log.d("DATA", "Stopover position = ${coord.latitude}, ${coord.longitude}")
            waypointMarker.position = coord
            waypointMarker.icon = OverlayImage.fromResource(R.drawable.study_marker)
            waypointMarker.map = naverMap
            stopoverpoint.add(coord)
            stopovermarker.add(waypointMarker)
        }

        var startObject = Building()

        // 하드코딩해야함! 현위치 좌표
        if (positionParser(startString) == -2) startString = "현위치"
        if (positionParser(startString) == -1) {
            startMarker = Marker().apply {
                icon = OverlayImage.fromResource(R.drawable.icon_startpoint)
            }
            // 하드코딩 lat, lng
            //startMarker?.position = LatLng(37.29422312, 126.9749711)
            startMarker?.position = LatLng(37.29662728572387, 126.97542827360417)

            startMarker?.map = naverMap
        }
        else if (positionParser(startString) == 0) {
            startObject = Building(
                buildingNum = startString.substring(0, 2)
            )
        }
        else if (positionParser(startString) == 1) {
            startObject = Building(
                roomNum = startString
            )
        }
        if (positionParser(startString) != -1) {
            val searchbuilding = ApiObject.service.searchBuilding(startObject)
            searchbuilding.clone().enqueue(object : Callback<BuildingResponse> {
                override fun onResponse(
                    call: Call<BuildingResponse>,
                    response: Response<BuildingResponse>
                ) {
                    val buildingresponse = response.body()
                    var lat = 0.0
                    var lng = 0.0
                    lat = buildingresponse?.latitude!!
                    lng = buildingresponse?.longitude!!
                    avglat+=lat; avglng+=lng
                    if (response.isSuccessful.not()) {
                        Log.d("DATA", "code=${response.code()}, message = ${lat}, ${lng}")
                        return
                    }
                    Log.d("DATA", "----------${lat}, ${lng}----------")
                    startMarker = Marker().apply {
                        icon = OverlayImage.fromResource(R.drawable.icon_startpoint)
                    }
                    startMarker?.position = LatLng(lat, lng)
                    startMarker?.map = naverMap
                }

                override fun onFailure(call: Call<BuildingResponse>, t: Throwable) {
                    Log.e("ERROR", t.toString())
                }
            })
        }
        var endObject = Building()

        if (positionParser(endString) == 0) {
            endObject = Building(
                buildingNum = endString.substring(0, 2)
            )
        }
        else if (positionParser(endString) == 1) {
            endObject = Building(
                roomNum = endString
            )
        }
        if (positionParser(endString) != 2) {
            val searchbuilding2 = ApiObject.service.searchBuilding(endObject)
            searchbuilding2.clone().enqueue(object : Callback<BuildingResponse> {
                override fun onResponse(
                    call: Call<BuildingResponse>,
                    response: Response<BuildingResponse>
                ) {
                    val buildingresponse = response.body()
                    var lat = 0.0
                    var lng = 0.0
                    avglat += lat; avglng += lng
                    lat = buildingresponse?.latitude!!
                    lng = buildingresponse.longitude
                    if (response.isSuccessful.not()) {
                        Log.d("DATA", "code=${response.code()}, message = ${lat}, ${lng}")
                        return
                    }
                    Log.d("DATA", "----------${lat}, ${lng}----------")
                    endMarker = Marker().apply {
                        icon = OverlayImage.fromResource(R.drawable.icon_endpoint)
                    }
                    endMarker?.position = LatLng(lat, lng)
                    endMarker?.map = naverMap
                }

                override fun onFailure(call: Call<BuildingResponse>, t: Throwable) {
                    Log.e("ERROR", t.toString())
                }
            })
        }
        else {
            endMarker = Marker().apply {
                icon = OverlayImage.fromResource(R.drawable.icon_endpoint)
            }
            endMarker?.position = LatLng(37.293885,126.974871)
            endMarker?.map = naverMap
        }
        //val initialCameraPosition = CameraPosition(LatLng(avglat/2, avglng/2), 16.0)
        val initialCameraPosition = CameraPosition(LatLng(37.29422312, 126.9749711), 15.2)
        naverMap.moveCamera(CameraUpdate.toCameraPosition(initialCameraPosition))
    }

    private fun undoStopover() {
        if (stopoverpoint.isNotEmpty()) {
            stopoverpoint.removeAt(stopoverpoint.size-1)
            stopovermarker[stopovermarker.size-1].map = null
            stopovermarker.removeAt(stopovermarker.size-1)
        }
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
        _binding = FragmentRouteresultBinding.inflate(inflater, container, false)
        val rootView = binding.root
        rootView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        var userId = LoginActivity.loginData.userId

        // Way control area
        if (!hasPermission()) {
            permissionLauncher.launch(permissions)
        } else {
            initMapView()
        }
        var undobutton = binding.cancel
        var applybutton = binding.apply
        undobutton.setOnClickListener {
            undoStopover()
        }
        applybutton.setOnClickListener {
            undobutton.visibility = View.GONE
            applybutton.visibility = View.GONE

            waypoints.add(startMarker?.position as LatLng)
            stopoverpoint.forEach { point ->
                waypoints.add(point)
            }
            waypoints.add(endMarker?.position as LatLng)
            val pathOverlay = PathOverlay()
            pathOverlay.coords = waypoints
            pathOverlay.map = naverMap

            var timeview = binding.timeTextView
            var distview = binding.distanceTextView
            val distance = calculateDistance(waypoints)
            val time = calculateEstimatedTime(distance)
            timeview.text = time.toString() + "분"
            distview.text = distance.toString() + "m"
        }

        // Bookmarkbutton control area
        val bookmarkbutton = binding.onbookmarkButton
        var state = 0
        applyDarkEffect(bookmarkbutton)
        bookmarkbutton.setOnClickListener {
            //add start, end at db
            if (state == 0) {

                val bookmarkObject = endMarker?.let { it1 ->
                    startMarker?.let { it2 ->
                        Bookmark(
                            start=startString,
                            end=endString,
                            startlatlng = it2.position,
                            endlatlng = it1.position,
                            stopover=stopoverpoint
                        )
                    }
                }
                val items = bookmarkViewModel.items
                if (bookmarkObject != null) {
                    items.add(bookmarkObject)
                    Log.d("DATA", "-------")
                }

                val saveObject = Savepath(
                    ID = userId,
                    Sequence= 1,
                    Stopover_count = 0,
                    Start_latitude = 37.29539,
                    Start_longitude = 126.97630,
                    End_latitude = 37.29539,
                    End_longitude = 126.97630,
                    Stopover = "_"
                )
                val addBookmark_ = ApiObject.service.savePath(saveObject)
                addBookmark_.clone().enqueue(object: Callback<SavepathResponse> {
                    override fun onResponse(call: Call<SavepathResponse>, response: Response<SavepathResponse>) {
                        val savepathresponse = response.body()
                        val statusmessage = savepathresponse?.message

                        if(response.isSuccessful.not()){
                            Log.d("DATA", "code=${response.code()}, message = $statusmessage")
                            return
                        }
                        Log.d("DATA", "code=${response.code()}, message = $statusmessage")
                    }
                    override fun onFailure(call: Call<SavepathResponse>, t: Throwable) {
                        Log.e("ERROR", t.toString())
                    }
                })
                showToast("즐겨찾기에 경로가 추가되었습니다.")
                removeDarkEffect(bookmarkbutton)
                state=1
            }
            //delete start, end at db
            else {
                val deleteObject = Deletepath(
                    ID= userId,
                    Sequence = 1
                )
                val deleteBookmark_ = ApiObject.service.deletePath(deleteObject)
                deleteBookmark_.clone().enqueue(object: Callback<DeletepathResponse> {
                    override fun onResponse(call: Call<DeletepathResponse>, response: Response<DeletepathResponse>) {
                        val deletepathResponse = response.body()
                        val statusmessage = deletepathResponse?.message

                        if(response.isSuccessful.not()){
                            Log.d("DATA", "code=${response.code()}, message = $statusmessage")
                            return
                        }
                        Log.d("DATA", "code=${response.code()}, message = $statusmessage")
                    }
                    override fun onFailure(call: Call<DeletepathResponse>, t: Throwable) {
                        Log.e("ERROR", t.toString())
                    }
                })
                showToast("즐겨찾기에 경로가 삭제되었습니다.")
                applyDarkEffect(bookmarkbutton)
                state=0
            }
        }

        // Entrancebutton control area
        val entrancebutton = binding.entrance2
        entrancebutton.setOnClickListener {
            if (!markersVisible) {
                addEntranceMarkersToMap()
                markersVisible = true
            } else {
                removeMarkersFromMap()
                markersVisible = false
            }
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapViewModel.startLocation.observe(viewLifecycleOwner) { text ->
            val editstart = view.findViewById<TextView>(R.id.startText)
            if (text.length < 2) {
                editstart.text = "현위치"
                startString = "현위치"
            }
            else {
                editstart.text = text
                endString = "현위치"
            }
        }
        mapViewModel.endLocation.observe(viewLifecycleOwner) { text ->
            val editend = view.findViewById<TextView>(R.id.endText)
            editend.text = text
            endString = text
        }
    }
}
package edu.skku.cs.skkedula.fragments.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import edu.skku.cs.skkedula.R
import edu.skku.cs.skkedula.api.ApiObject.service
import edu.skku.cs.skkedula.api.Building
import edu.skku.cs.skkedula.api.BuildingResponse
import edu.skku.cs.skkedula.api.RetrofitService
import edu.skku.cs.skkedula.api.Studyspace
import edu.skku.cs.skkedula.databinding.FragmentMapBinding
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

data class Point(val x: Double, val y: Double)

data class Entrance(
    val name: String,
    val number: Int,
    val entranceNumber: Int,
    val location: Point
)

class MapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private lateinit var retrofitService: RetrofitService

    private val locationPermissionRequestCode = 5000
    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private val mapViewModel: MapViewModel by activityViewModels()

    private var markersVisible = false
    private var studymarkersVisible = false
    private val markers = mutableListOf<Marker>()
    private val studymarkers = mutableListOf<Marker>()
    private var activeCardFragment: String? = null

    // ActivityResultLauncher 선언
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ActivityResultLauncher 초기화
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                val granted = permissions.entries.all { it.value }
                if (granted) {
                    initMapView()
                }
                // 여기에 권한 거부에 대한 처리 로직 추가 (옵션)
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrofit 인스턴스 초기화
        val retrofit = Retrofit.Builder()
            .baseUrl("https://example.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofitService = retrofit.create(RetrofitService::class.java)

        // Set up the EditText listener
        val searchEditText = binding.search // replace with your actual EditText ID
        searchEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                event?.action == KeyEvent.ACTION_DOWN &&
                event.keyCode == KeyEvent.KEYCODE_ENTER
            ) {

                // Trigger your search function
                //performTestSearch()
                performSearch(searchEditText.text.toString())

                // Hide the keyboard after search
                hideKeyboard()

                true // return true to indicate you've handled the event
            } else {
                false
            }
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
            transaction.replace(
                R.id.nav_host_fragment_activity_main,
                RoutesearchFragment()
            )
            transaction.commit()
        }

        // 위치 권한 확인 및 요청
        if (!hasPermission()) {
            permissionLauncher.launch(permissions)
        } else {
            initMapView()
        }

        return binding.root
    }

    private fun initMapView() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as MapFragment?
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

        binding.entrance.setOnClickListener {
            if (!markersVisible) {
                addEntranceMarkersToMap()
                markersVisible = true
            } else {
                removeMarkersFromMap()
                markersVisible = false
            }
        }

        binding.study.setOnClickListener {
            if (!markersVisible) {
                fetchAndDisplayStudySpaces()
            } else {
                removeMarkersFromMap()
            }
            markersVisible = !markersVisible
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    private fun fetchAndDisplayStudySpaces() {
        // Assuming you have a Retrofit service instance named 'service'
        if (!studymarkersVisible) {
            service.getStudyspace().enqueue(object : Callback<List<Studyspace>> {
                override fun onResponse(
                    call: Call<List<Studyspace>>,
                    response: Response<List<Studyspace>>
                ) {
                    if (response.isSuccessful) {
                        val studySpaces = response.body()
                        studySpaces?.let { spaces ->
                            addStudyMarkersToMap(spaces)
                            studymarkersVisible = true
                        }
                    } else {
                        // Handle API error response
                    }
                }

                override fun onFailure(call: Call<List<Studyspace>>, t: Throwable) {
                    // Handle network error
                }
            })
        } else {
            removeStudyMarkersFromMap()
            studymarkersVisible = false
        }
    }

    private fun addStudyMarkersToMap(studySpaces: List<Studyspace>) {
        studySpaces.forEach { space ->
            val marker = Marker().apply {
                position = LatLng(space.latitude, space.longitude)
                icon = OverlayImage.fromResource(R.drawable.study_marker)
                map = naverMap
                tag = space
            }
            marker.onClickListener = Overlay.OnClickListener { overlay ->
                val clickedMarker = overlay as? Marker
                clickedMarker?.let {
                    showCardView(it)
                    Log.d("MapFragment", "Study marker clicked: ${it.tag}")
                    // ViewModel의 onMarkerClicked 호출
                    mapViewModel.onMarkerClicked(it.tag as? Studyspace)
                }
                true // 항상 non-nullable Boolean 값을 반환
            }
            studymarkers.add(marker)
        }
    }

    private fun removeStudyMarkersFromMap() {
        studymarkers.forEach { it.map = null }
        studymarkers.clear()
    }

    private fun showCardView(marker: Marker) {
        val study = marker.tag as? Studyspace ?: return
        val bundle = Bundle().apply {
            putString("studyName", study.name) // "studyName"은 전달할 키입니다.
            putString("address", study.address)
            putString("time", study.time)
        }

        // TextView 업데이트
        val studyNameTextView = activity?.findViewById<TextView>(R.id.studyname)
        val addressTextView = activity?.findViewById<TextView>(R.id.address)
        val timeTextView = activity?.findViewById<TextView>(R.id.time)

        studyNameTextView?.text = study.name
        addressTextView?.text = study.address
        timeTextView?.text = study.time

        // Make the card view visible
        val cardView = activity?.findViewById<FragmentContainerView>(R.id.card)
        cardView?.visibility = View.VISIBLE

        // Navigate to StudyDetailFragment using NavController
        // Check if the FragmentContainerView for the card is a NavHostFragment
        val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.card) as? NavHostFragment
        navHostFragment?.let {
            it.navController.navigate(R.id.studyDetailFragment, bundle)
        } ?: run {
            Log.e("MapFragment", "NavHostFragment not found or not set up correctly")
        }
    }


    /*private fun performTestSearch() {
        // JSON 형태로 Building 객체 생성
        val json = "{\"Building_num\":\"85\"}"

        // Gson 라이브러리를 사용하여 JSON을 Building 객체로 변환
        val gson = Gson()
        var testBuildingRequest = gson.fromJson(json, Building::class.java)

        // buildingNum 속성을 설정
        testBuildingRequest.buildingNum = "85"

        // 로그로 Building 객체 상태 출력
        Log.d("SearchDebug", "Test Building Request: $testBuildingRequest")
        Log.d("RequestDebug", "API Request JSON: $json")

        // Retrofit 요청을 testBuildingRequest로 수행
        retrofitService.searchBuilding(testBuildingRequest).enqueue(object : Callback<BuildingResponse> {
            override fun onResponse(call: Call<BuildingResponse>, response: Response<BuildingResponse>) {
                if (response.isSuccessful) {
                    // 성공적인 응답 처리
                    val result = response.body()
                    Log.d("SearchDebug", "Response: Latitude - ${result?.latitude}, Longitude - ${result?.longitude}")
                } else {
                    // 에러 응답 처리
                    Log.d("SearchDebug", "No search result")
                }
            }

            override fun onFailure(call: Call<BuildingResponse>, t: Throwable) {
                // 네트워크 오류 처리
                Log.d("SearchDebug", "Network Error: ${t.message}")
            }
        })
    }*/

    private fun performSearch(searchText: String) {

        // 기존 카드뷰 숨기기
        hideCardView()

        //데이터 하드코딩
        if (searchText == "27323") {
            // 하드 코딩된 데이터로 위치 정보 설정
            val latitude = 37.2954353 // 임의의 위도
            val longitude = 126.9763128 // 임의의 경도
            val buildingName = "제2공학관27동" // 건물 이름

            // 지도에 하드 코딩된 위치 정보를 추가
            addMarkerToMap(latitude, longitude, buildingName)

            // ViewModel에 건물 데이터 전달
            mapViewModel.onBuildingDataReceived(BuildingResponse(buildingName, latitude, longitude))
        } else if (searchText == "40") {
            // 두 번째 하드 코딩된 데이터로 위치 정보 설정
            val latitude = 37.291664 // 임의의 위도
            val longitude = 126.977916 // 임의의 경도
            val buildingName = "반도체관" // 건물 이름

            // 지도에 두 번째 하드 코딩된 위치 정보를 추가
            addMarkerToMap(latitude, longitude, buildingName)

            // ViewModel에 건물 데이터 전달
            mapViewModel.onBuildingDataReceived(BuildingResponse(buildingName, latitude, longitude))
        // 사용자 입력에 따라 적절한 Building 객체를 생성합니다.
        /*val buildingRequest = when {
            searchText.isDigitsOnly() && searchText.length > 4 -> Building.fromRoomNum(searchText.toInt())
            searchText.isDigitsOnly() -> Building.fromBuildingNum(searchText.toInt())
            else -> Building.fromName(searchText)
        }

        // Retrofit 요청을 보내기 전에 요청 본문을 확인하고 로그 출력
        val requestBody = Gson().toJson(buildingRequest)
        Log.d("SearchDebug", "Request Body: $requestBody")

        // Retrofit을 사용하여 서버에 검색 요청을 보냅니다.
        retrofitService.searchBuilding(buildingRequest)
            .enqueue(object : Callback<BuildingResponse> {
                override fun onResponse(
                    call: Call<BuildingResponse>,
                    response: Response<BuildingResponse>
                ) {
                    if (response.isSuccessful) {
                        val buildingResponse = response.body()
                        buildingResponse?.let {
                            // 지도에 마커 추가
                            addMarkerToMap(it.buildingName, it.latitude, it.longitude)
                            // ViewModel에 건물 데이터 전달
                            mapViewModel.onBuildingDataReceived(it)
                        }
                    } else {
                        // 에러 응답 처리
                        showToast("검색 결과가 없습니다")
                    }
                }

                override fun onFailure(call: Call<BuildingResponse>, t: Throwable) {
                    // 네트워크 오류 처리
                    showToast("네트워크 오류: ${t.message}")
                }*/
            }//)
    }

    //하드코딩용
    private fun addMarkerToMap(
        latitude: Double,
        longitude: Double,
        buildingName: String
    ) {
        val marker = Marker().apply {
            position = LatLng(latitude, longitude)
            map = naverMap
            icon = OverlayImage.fromResource(R.drawable.icon_startpoint)
            tag = buildingName // 건물 이름을 태그로 저장
        }
        marker.onClickListener = Overlay.OnClickListener { overlay ->
            val clickedMarker = overlay as? Marker
            clickedMarker?.let {
                // 클릭한 마커의 태그로 건물 이름을 가져옴
                val buildingName = it.tag as? String
                buildingName?.let {
                    // 클릭한 건물 이름으로 BuildingDetailFragment로 이동
                    showBuildingCardView(buildingName)
                }
            }
            true
        }
        markers.add(marker)
    }

    private fun showBuildingCardView(buildingName: String) {
        Log.d("MapFragment", "Navigating to BuildingDetailFragment with buildingName: $buildingName")

        val bundle = Bundle().apply {
            putString("buildingName", buildingName) // "buildingName"은 전달할 키입니다.
        }

        // TextView 업데이트
        val buildingNameTextView = activity?.findViewById<TextView>(R.id.buildingname)

        buildingNameTextView?.text = buildingName

        // Make the card view visible
        val cardView = activity?.findViewById<FragmentContainerView>(R.id.card)
        cardView?.visibility = View.VISIBLE

        // Navigate to BuildingDetailFragment using NavController
        // Check if the FragmentContainerView for the card is a NavHostFragment
        val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.card) as? NavHostFragment
        navHostFragment?.let {
            it.navController.navigate(R.id.buildingDetailFragment, bundle)
        } ?: run {
            Log.e("MapFragment", "NavHostFragment not found or not set up correctly")
        }
    }

    /*private fun addMarkerToMap(buildingName: String, latitude: Double, longitude: Double) {
        val marker = Marker().apply {
            position = LatLng(latitude, longitude)
            map = naverMap
            icon = OverlayImage.fromResource(R.drawable.icon_startpoint)
            tag = buildingName // Building 이름 저장
        }
        marker.onClickListener = Overlay.OnClickListener { overlay ->
            val clickedMarker = overlay as? Marker
            clickedMarker?.let {
                navigateToBuildingDetail(it.tag as String)
            }
            true
        }
    }

    private fun navigateToBuildingDetail(buildingName: String) {
        val bundle = Bundle().apply {
            putString("buildingName", buildingName)
        }
        findNavController().navigate(R.id.buildingDetailFragment, bundle)
    }


    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }*/


    private fun hideKeyboard() {
        val inputMethodManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun hideCardView() {
        val cardView = activity?.findViewById<FragmentContainerView>(R.id.card)
        cardView?.visibility = View.GONE
    }

}


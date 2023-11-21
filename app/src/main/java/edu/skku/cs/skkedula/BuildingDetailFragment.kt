package edu.skku.cs.skkedula

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import edu.skku.cs.skkedula.databinding.FragmentBuildingDetailBinding
import edu.skku.cs.skkedula.fragments.map.MapViewModel
import edu.skku.cs.skkedula.fragments.map.RoutesearchFragment

class BuildingDetailFragment : Fragment() {
    private var buildingName: String? = null
    private var _binding: FragmentBuildingDetailBinding? = null
    private val binding get() = _binding!!
    private val mapViewModel: MapViewModel by activityViewModels()


    companion object {
        private const val ARG_BUILDING_NAME = "buildingName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            buildingName = it.getString(ARG_BUILDING_NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuildingDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buildingname.text = buildingName

        // Starting and Ending button click listeners
        binding.starting.setOnClickListener {
            navigateToRouteSearch(true, buildingName)
        }

        binding.ending.setOnClickListener {
            navigateToRouteSearch(false, buildingName)
        }

        // 기존 관찰자 제거
        mapViewModel.buildingData.removeObservers(viewLifecycleOwner)

        // Observe the LiveData for marker click
        mapViewModel.buildingData.observe(viewLifecycleOwner) { buildingResponse ->
            // buildingResponse 객체를 사용하여 UI 업데이트
            if (buildingResponse != null) {
                // 예: 건물 이름과 다른 정보를 UI에 표시
                binding.buildingname.text = buildingResponse.buildingName
                // 추가적인 UI 업데이트...
            }
        }
    }

    private fun hideCardView() {
        val cardView = activity?.findViewById<FragmentContainerView>(R.id.card)
        cardView?.visibility = View.GONE
    }

    private fun navigateToRouteSearch(isStarting: Boolean, buildingName: String?) {
        val bundle = Bundle()
        bundle.putString(if (isStarting) "editstart" else "editend", buildingName)

        val routesearchFragment = RoutesearchFragment()
        routesearchFragment.arguments = bundle

        // RoutesearchFragment로 이동 전 카드뷰 숨기기
        hideCardView()


        // RoutesearchFragment로 이동
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment_activity_main, routesearchFragment)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
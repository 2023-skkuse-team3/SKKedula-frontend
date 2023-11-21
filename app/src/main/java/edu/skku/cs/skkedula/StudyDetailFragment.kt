package edu.skku.cs.skkedula

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import edu.skku.cs.skkedula.databinding.FragmentBuildingDetailBinding
import edu.skku.cs.skkedula.databinding.FragmentStudyDetailBinding
import edu.skku.cs.skkedula.fragments.map.MapViewModel
import edu.skku.cs.skkedula.fragments.map.RoutesearchFragment

class StudyDetailFragment : Fragment() {
    private var studyName: String? = null
    private var _binding: FragmentStudyDetailBinding? = null
    private val binding get() = _binding!!
    private val mapViewModel: MapViewModel by activityViewModels()

    // ARG_STUDY_NAME 상수 추가
    companion object {
        const val ARG_STUDY_NAME = "studyName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            studyName = it.getString(ARG_STUDY_NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStudyDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Update the study name
        binding.studyname.text = studyName

        // Starting and Ending button click listeners
        binding.starting.setOnClickListener {
            // Navigate to RouteSearchFragment with "editstart" data
            mapViewModel.startLocation.value = studyName
            mapViewModel.endLocation.value = ""
            navigateToRouteSearch(true, studyName)
        }

        binding.ending.setOnClickListener {
            // Navigate to RouteSearchFragment with "editend" data
            mapViewModel.startLocation.value = ""
            mapViewModel.endLocation.value = studyName
            navigateToRouteSearch(false, studyName)
        }

        // 기존 관찰자 제거
        mapViewModel.markerClicked.removeObservers(viewLifecycleOwner)

        // Observe the LiveData for marker click
        // Observe the LiveData for marker click
        mapViewModel.markerClicked.observe(viewLifecycleOwner) { clickedStudyspace ->
            if (clickedStudyspace != null) {
                // 클릭된 스터디 공간의 이름을 사용하여 UI 업데이트
                binding.studyname.text = clickedStudyspace.name
                // 추가적인 UI 업데이트...
            }
        }
    }

    private fun hideCardView() {
        val cardView = activity?.findViewById<FragmentContainerView>(R.id.card)
        cardView?.visibility = View.GONE
    }

    private fun navigateToRouteSearch(isStarting: Boolean, studyName: String?) {
        val bundle = Bundle()
        bundle.putString(if (isStarting) "editstart" else "editend", studyName)

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
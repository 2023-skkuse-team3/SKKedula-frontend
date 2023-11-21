package edu.skku.cs.skkedula

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            navigateToRouteSearch(true, studyName)
        }

        binding.ending.setOnClickListener {
            // Navigate to RouteSearchFragment with "editend" data
            navigateToRouteSearch(false, studyName)
        }

        // Observe the LiveData for marker click
        mapViewModel.markerClicked.observe(viewLifecycleOwner) { clickedStudyName ->
            // Handle marker click event
            // You can use clickedStudyName as needed
        }
    }

    private fun navigateToRouteSearch(isStarting: Boolean, studyName: String?) {
        val bundle = Bundle()
        bundle.putString(if (isStarting) "editstart" else "editend", studyName)

        val routesearchFragment = RoutesearchFragment()
        routesearchFragment.arguments = bundle

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
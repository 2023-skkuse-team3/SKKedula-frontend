package edu.skku.cs.skkedula

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import edu.skku.cs.skkedula.databinding.FragmentBuildingDetailBinding
import edu.skku.cs.skkedula.databinding.FragmentStudyDetailBinding

class StudyDetailFragment : Fragment() {
    private var studyName: String? = null
    private var _binding: FragmentStudyDetailBinding? = null
    private val binding get() = _binding!!

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
            navigateToRouteSearch(true)
        }

        binding.ending.setOnClickListener {
            navigateToRouteSearch(false)
        }
    }

    private fun navigateToRouteSearch(isStarting: Boolean) {
        val bundle = Bundle()
        bundle.putString(if (isStarting) "editstart" else "editend", studyName)
        findNavController().navigate(R.id.RoutesearchFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
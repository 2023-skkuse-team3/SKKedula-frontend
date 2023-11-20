package edu.skku.cs.skkedula

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import edu.skku.cs.skkedula.databinding.FragmentBuildingDetailBinding

class BuildingDetailFragment : Fragment() {
    private var buildingName: String? = null
    private var _binding: FragmentBuildingDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            buildingName = it.getString(ARG_ENTRANCE_NAME)
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

        // Update the entrance name
        binding.buildingname.text = buildingName

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
        bundle.putString(if (isStarting) "editstart" else "editend", buildingName)
        findNavController().navigate(R.id.RoutesearchFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_ENTRANCE_NAME = "buildingName"
        @JvmStatic
        fun newInstance(buildingName: String) =
            BuildingDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ENTRANCE_NAME, buildingName)
                }
            }
    }
}
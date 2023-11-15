package edu.skku.cs.skkedula

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import edu.skku.cs.skkedula.databinding.FragmentEntranceDetailBinding // Import the generated binding class

class EntranceDetailFragment : Fragment() {
    private var entranceName: String? = null
    private var _binding: FragmentEntranceDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            entranceName = it.getString(ARG_ENTRANCE_NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEntranceDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Update the entrance name
        binding.entrancename.text = entranceName

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
        bundle.putString(if (isStarting) "editstart" else "editend", entranceName)
        findNavController().navigate(R.id.RoutesearchFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_ENTRANCE_NAME = "entranceName"
        @JvmStatic
        fun newInstance(entranceName: String) =
            EntranceDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ENTRANCE_NAME, entranceName)
                }
            }
    }
}
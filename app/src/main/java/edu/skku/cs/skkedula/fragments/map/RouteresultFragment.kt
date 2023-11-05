package edu.skku.cs.skkedula.fragments.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import edu.skku.cs.skkedula.R
import edu.skku.cs.skkedula.databinding.FragmentRouteresultBinding

class RouteresultFragment : Fragment() {
    private lateinit var _binding: FragmentRouteresultBinding
    private val binding get() = _binding!!
    private val mapViewModel: MapViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRouteresultBinding.inflate(inflater, container, false)
        val rootView = binding.root
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapViewModel.startText.observe(viewLifecycleOwner) { text ->
            val editstart = view.findViewById<TextView>(R.id.startText)
            editstart.text = text
        }
        mapViewModel.endText.observe(viewLifecycleOwner) { text ->
            val editend = view.findViewById<TextView>(R.id.endText)
            editend.text = text
        }
    }
}
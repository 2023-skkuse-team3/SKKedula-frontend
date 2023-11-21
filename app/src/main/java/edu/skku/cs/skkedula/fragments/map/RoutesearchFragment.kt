package edu.skku.cs.skkedula.fragments.map

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import edu.skku.cs.skkedula.R
import edu.skku.cs.skkedula.databinding.FragmentRoutesearchBinding

class RoutesearchFragment : Fragment() {
    private lateinit var _binding: FragmentRoutesearchBinding
    private val binding get() = _binding!!
    private val mapViewModel: MapViewModel by activityViewModels()
    //private val mapViewModel: MapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRoutesearchBinding.inflate(inflater, container, false)
        val buttonSearch = binding.searchbutton
        buttonSearch.setOnClickListener{
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment_activity_main, RouteresultFragment())
            //transaction.addToBackStack("fragment_routesearch")
            transaction.commit()
        }
        val rootView = binding.root
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val editstart = view.findViewById<EditText>(R.id.editstart)
        val editend = view.findViewById<EditText>(R.id.editend)
        //editstart.addTextChangedListener { mapViewModel.startText.value = it.toString() }
        //editend.addTextChangedListener { mapViewModel.endText.value = it.toString() }

        val startTextValue = mapViewModel.startLocation.value
        val endTextValue = mapViewModel.endLocation.value
        editstart.setText(startTextValue)
        editend.setText(endTextValue)

        arguments?.getString("editstart")?.let {
            view.findViewById<EditText>(R.id.editstart).setText(it)
        }

        arguments?.getString("editend")?.let {
            view.findViewById<EditText>(R.id.editend).setText(it)
        }
    }
}
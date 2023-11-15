package edu.skku.cs.skkedula.fragments.map

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import edu.skku.cs.skkedula.R
import edu.skku.cs.skkedula.databinding.FragmentRouteresultBinding

class RouteresultFragment : Fragment() {
    private lateinit var _binding: FragmentRouteresultBinding
    private val binding get() = _binding!!
    private val mapViewModel: MapViewModel by activityViewModels()

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRouteresultBinding.inflate(inflater, container, false)

        // Bookmarkbutton control area
        val bookmarkbutton = binding.onbookmarkButton
        var state = 0
        applyDarkEffect(bookmarkbutton)
        bookmarkbutton.setOnClickListener {
            //todo: add start, end at db
            if (state == 0) {
                showToast("즐겨찾기에 경로가 추가되었습니다.")
                removeDarkEffect(bookmarkbutton)
                state=1
            }
            //todo: delete start, end at db
            else {
                showToast("즐겨찾기에 경로가 삭제되었습니다.")
                applyDarkEffect(bookmarkbutton)
                state=0
            }
        }

        // Entrancebutton control area
        val entrancebutton = binding.entrance2
        entrancebutton.setOnClickListener {

        }

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
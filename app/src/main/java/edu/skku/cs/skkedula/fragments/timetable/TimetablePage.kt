package edu.skku.cs.skkedula.fragments.timetable

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import edu.skku.cs.skkedula.R
import edu.skku.cs.skkedula.databinding.FragmentBookmarkBinding
import edu.skku.cs.skkedula.databinding.FragmentTimetablePageBinding

/**
 * A simple [Fragment] subclass.
 * Use the [TimetablePage.newInstance] factory method to
 * create an instance of this fragment.
 */
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class TimetablePage : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentTimetablePageBinding? = null
    private val timetableViewModel: TimetableViewModel by activityViewModels()
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTimetablePageBinding.inflate(inflater, container, false)

        if (timetableViewModel.userCourseList.value.isNullOrEmpty()) {
            // set empty timetable
            val emptyTimetable = EmptyTimetable()
            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView, emptyTimetable)
                .commit()
        } else {
            // set timetable
            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView, Timetable())
                .commit()

            // timetable edit button 표시
            val editButton = binding.editButton
            editButton?.visibility = View.VISIBLE
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // timetable edit button
        val editButton = view.findViewById<ImageButton>(R.id.editButton)

        // timetable 수정 버튼
        editButton.setOnClickListener {
            val targetView = requireActivity().findViewById<FragmentContainerView>(R.id.card)
            val buttomUpAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.bottom_up)
            targetView.startAnimation(buttomUpAnimation)
            targetView.visibility = View.VISIBLE

            // apply navigation bar
            val navController = Navigation.findNavController(requireActivity(), R.id.card)
            navController.navigate(R.id.timetableMenu)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TimeTable.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TimetablePage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
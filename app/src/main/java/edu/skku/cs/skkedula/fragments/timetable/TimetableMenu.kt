package edu.skku.cs.skkedula.fragments.timetable

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.google.android.material.button.MaterialButton
import edu.skku.cs.skkedula.R
import androidx.fragment.app.commit


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TimetableMenu.newInstance] factory method to
 * create an instance of this fragment.
 */
class TimetableMenu : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        return inflater.inflate(R.layout.fragment_timetable_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 강의 추가하기 메뉴
        val courseBtn = view.findViewById<MaterialButton>(R.id.addCourse)
        courseBtn.setOnClickListener {
            // apply navigation bar
            val navController = Navigation.findNavController(requireActivity(), R.id.card)
            navController.navigate(R.id.courseSearchFragment)
        }

        // 커스텀 일정 추가하기 메뉴
        val scheduleBtn = view.findViewById<MaterialButton>(R.id.addCustomSchedule)
        scheduleBtn.setOnClickListener {
            // apply navigation bar
            val navController = Navigation.findNavController(requireActivity(), R.id.card)
            navController.navigate(R.id.customScheduleFragment)
        }

        // 시간표 지우기 메뉴
        val deleteBtn = view.findViewById<MaterialButton>(R.id.deleteTimetable)
        deleteBtn.setOnClickListener {
            // card 끄기
            val targetView = requireActivity().findViewById<FragmentContainerView>(R.id.card)
            targetView.visibility = View.GONE

            // 삭제 확인 메시지 설정
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("시간표 삭제")
                .setMessage("시간표를 삭제하시겠습니까?")
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, id ->
                        // 시간표 삭제하기

                        // empty timetable로 이동
                        val emptyTimetable = EmptyTimetable()
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView, emptyTimetable)
                            .addToBackStack(null) // 이전 fragment로 돌아갈 수 있도록 스택에 추가
                            .commit()
                    })
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, id ->
                        Log.d("TAB", "취소 클릭")
                    })
            // 삭제 확인 메시지 띄우기
            builder.show()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TimetableMenu.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TimetableMenu().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
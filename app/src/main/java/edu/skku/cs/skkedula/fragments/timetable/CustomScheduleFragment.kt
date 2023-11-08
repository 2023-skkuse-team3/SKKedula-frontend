package edu.skku.cs.skkedula.fragments.timetable

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.Navigation
import edu.skku.cs.skkedula.R
import edu.skku.cs.skkedula.databinding.FragmentCustomScheduleBinding
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CustomScheduleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CustomScheduleFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var startTimeString = ""
    var endTimeString = ""

    lateinit var binding:FragmentCustomScheduleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // val view = inflater.inflate(R.layout.fragment_custom_schedule, container, false)
        binding=FragmentCustomScheduleBinding.inflate(inflater)
        val startTime = binding.startTime
        val endTime = binding.endTime
        val addBtn = binding.addSchedule

        startTime.setOnClickListener {
            // 시간 선택 dialog 표시
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                startTimeString = "${hourOfDay}:${minute}"

                // 시간 textview에 표시
                val startTime2 = binding.startTime
                startTime2.text = startTimeString + " 부터"
            }
            TimePickerDialog(activity, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()

            // 시작 시간 저장
        }

        endTime.setOnClickListener {
            // 시간 선택 dialog 표시
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                endTimeString = "${hourOfDay}:${minute}"

                // 시간 textview에 표시
                val endTime2 = binding.endTime
                endTime2.text = endTimeString + " 까지"
            }
            TimePickerDialog(activity, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()

            // 종료 시간 저장
        }

        addBtn.setOnClickListener {

        }

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CustomScheduleFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
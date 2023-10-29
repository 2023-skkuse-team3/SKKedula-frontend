package edu.skku.cs.skkedula

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.appbar.MaterialToolbar
import com.islandparadise14.mintable.MinTimeTableView
import com.islandparadise14.mintable.model.ScheduleDay
import com.islandparadise14.mintable.model.ScheduleEntity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Timetable.newInstance] factory method to
 * create an instance of this fragment.
 */
class Timetable : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val day = arrayOf("월", "화", "수", "목", "금")

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
        val view = inflater.inflate(R.layout.fragment_timetable, container, false)
        val table = view.findViewById<MinTimeTableView>(R.id.table)
        table.initTable(day)

        val scheduleList: ArrayList<ScheduleEntity> = ArrayList()
        val schedule = ScheduleEntity(
            32, //originId
            "소프트웨어공학개론", //scheduleName
            "제2공학관 3층 26312", //roomInfo
            ScheduleDay.TUESDAY, //ScheduleDay object (MONDAY ~ SUNDAY)
            "8:20", //startTime format: "HH:mm"
            "10:30", //endTime  format: "HH:mm"
            "#73fcae68", //backgroundColor (optional)
            "#000000" //textcolor (optional)
        )

        scheduleList.add(schedule)

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Timetable.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Timetable().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
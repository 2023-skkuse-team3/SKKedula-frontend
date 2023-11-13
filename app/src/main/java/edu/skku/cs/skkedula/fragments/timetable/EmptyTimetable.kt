package edu.skku.cs.skkedula.fragments.timetable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import edu.skku.cs.skkedula.api.ApiObject
import edu.skku.cs.skkedula.R


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EmptyTimetable.newInstance] factory method to
 * create an instance of this fragment.
 */
class EmptyTimetable : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var everytimeUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_empty_timetable, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // api interface 가져오기
        val callSearchCourse = ApiObject.service.searchCourse("introduction")


        // 버튼 클릭 시 Fragment 전환
        val button = view.findViewById<Button>(R.id.addDirectly)
        val urlButton = view.findViewById<Button>(R.id.addWithUrl)

        button.setOnClickListener {

            // timetable edit button 표시
            val editButton = requireActivity().findViewById<ImageButton>(R.id.editButton)
            editButton?.visibility = View.VISIBLE

            // timetable fragment로 교체
            val timetable = Timetable()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, timetable)
                .commit()
        }

        urlButton.setOnClickListener {
            // url 가져오기
            val urlInput = view.findViewById<EditText>(R.id.InputUrl)

            // url 보내기

            // timetable fragment로 교체
            val timetable = Timetable()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, timetable)
                .commit()

            // timetable edit button 표시
            val editButton = requireActivity().findViewById<ImageButton>(R.id.editButton)
            editButton?.visibility = View.VISIBLE

            // 사용자의 강의 목록 불러와 시간표에 추가하기
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EmptyTimetable.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EmptyTimetable().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
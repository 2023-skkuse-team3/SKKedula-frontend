package edu.skku.cs.skkedula.fragments.timetable

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.skku.cs.skkedula.R
import edu.skku.cs.skkedula.api.Course

class RecyclerViewAdapter(val itemList: List<Course>): RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.courseName.text = itemList[position].courseName.split('\n')[0]
        holder.professor.text = itemList[position].professor
        if (itemList[position].roomNum != "")
            holder.roomNum.text = itemList[position].roomNum
        holder.courseType.text = itemList[position].classType
        holder.time.text = itemList[position].time
    }

    private fun formatTimeString(input: String): String {
        val parts = input.split(", ")
        val formattedTimes = parts.map { part ->
            val (count, times) = part.split("_")
            val dayString = when (count.toInt()) {
                1 -> "월"
                2 -> "화"
                3 -> "수"
                4 -> "목"
                5 -> "금"
                else -> " "
            }
            val startTime = times.substring(0, 2) + ":" + times.substring(2, 4)
            val endTime = times.substring(4, 6) + ":" + times.substring(6, 8)
            "$dayString $startTime-$endTime"
        }
        return formattedTimes.joinToString("/ ")
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val courseName = itemView.findViewById<TextView>(R.id.courseName2)
        val professor = itemView.findViewById<TextView>(R.id.professor2)
        val roomNum = itemView.findViewById<TextView>(R.id.classroom2)
        val time = itemView.findViewById<TextView>(R.id.time2)
        val courseType = itemView.findViewById<TextView>(R.id.courseType2)
        val addBtn = itemView.findViewById<Button>(R.id.addCourse2)
    }

}
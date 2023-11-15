package edu.skku.cs.skkedula.fragments.bookmark

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import edu.skku.cs.skkedula.R

class BookmarkAdapter(val context: Context, val items:ArrayList<Bookmark>): BaseAdapter() {
    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(p0: Int): Any {
        return items.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.bookmark_item,null)
        val start = view.findViewById<TextView>(R.id.bookstartText)
        val end = view.findViewById<TextView>(R.id.bookendText)

        start.setText(items.get(p0).start)
        end.setText(items.get(p0).end)

        val removebutton = view.findViewById<ImageView>(R.id.trash)
        removebutton.setOnClickListener {
            //todo: remove in db
        }

        return view
    }

}
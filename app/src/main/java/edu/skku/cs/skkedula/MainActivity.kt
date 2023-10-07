package edu.skku.cs.skkedula

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    fun navigateLoginActivity(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // change "SKK" to green
        val spannable1 = SpannableString("SKKEDULA")
        val colorSpan1 = ForegroundColorSpan(Color.rgb(43,102,83))
        spannable1.setSpan(colorSpan1, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        findViewById<TextView>(R.id.textView1).text = spannable1

        // change "위치 기반" to yellow
        val spannable2 = SpannableString("교내 위치 기반 일정 관리 서비스")
        val colorSpan2 = ForegroundColorSpan(Color.rgb(253,177,50))
        spannable2.setSpan(colorSpan2, 3, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        findViewById<TextView>(R.id.textView2).text = spannable2
    }
}
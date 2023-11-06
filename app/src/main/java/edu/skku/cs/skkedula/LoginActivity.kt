package edu.skku.cs.skkedula

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class LoginActivity : AppCompatActivity() {

    // todo: implement auth
    private fun isValid(username: String, password: String): Boolean {
        if (username.equals("admin") && password.equals("admin")) return true
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // change "SKK" to green
        val spannable1 = SpannableString("SKKEDULA")
        val colorSpan1 = ForegroundColorSpan(Color.rgb(43,102,83))
        spannable1.setSpan(colorSpan1, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        findViewById<TextView>(R.id.textView1).text = spannable1

        // press "회원가입", go to signup activity
        val textClickable = findViewById<TextView>(R.id.textView5)
        textClickable.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        })

        // check id, password
        val usernameEditText = findViewById<EditText>(R.id.editTextUsername)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        val loginButton = findViewById<Button>(R.id.loginbutton)
        val errorMessageTextView = findViewById<TextView>(R.id.errormsg)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (isValid(username, password)) {
                errorMessageTextView.text = ""
                finish()
                val intent = Intent(this, SkkedulaActivity::class.java)
                startActivity(intent)
            } else {
                errorMessageTextView.text = "아이디나 비밀번호가 올바르지 않습니다."
                errorMessageTextView.visibility = View.VISIBLE
            }
        }
    }
}
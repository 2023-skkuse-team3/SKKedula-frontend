package edu.skku.cs.skkedula

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class SignupActivity : AppCompatActivity() {
    // todo: check id is duplicated
    private fun isValidId(username: String): Boolean {
        if (username.equals("admin")) return true
        return false
    }

    // todo: check pw is in condition
    private fun isValidPw(password: String): Boolean {
        if (password.length < 8 || password.length > 12) return false
        return true
    }

    private fun isSamePw(password1: String, password2: String): Boolean {
        if (password1.equals(password2)) return true
        else return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val usernameText = findViewById<EditText>(R.id.registerid)
        val password1Text = findViewById<EditText>(R.id.registerpassword)
        val password2Text = findViewById<EditText>(R.id.againpassword)
        val registerbutton = findViewById<Button>(R.id.registerbutton)
        val iderrorText = findViewById<TextView>(R.id.iderror)
        val pwerrorText = findViewById<TextView>(R.id.pwerror)
        val againpwerrorText = findViewById<TextView>(R.id.againpwerror)

        registerbutton.setOnClickListener {
            iderrorText.text = ""
            pwerrorText.text = ""
            againpwerrorText.text = ""

            val username = usernameText.text.toString()
            val password1 = password1Text.text.toString()
            val password2 = password2Text.text.toString()
            if (isValidId(username) && isValidPw(password1) && isSamePw(password1,password2)) {
                //todo: db에 id, pw 저장
                Toast.makeText(applicationContext, "회원가입 성공! 로그인 화면으로 돌아가세요.", Toast.LENGTH_SHORT).show()
            }
            else {
                if (!isValidId(username)) {
                    iderrorText.text = "사용할 수 없는 아이디입니다"
                }
                else if (!isValidPw(password1)) {
                    pwerrorText.text = "사용할 수 없는 비밀번호입니다"
                }
                else {
                    againpwerrorText.text = "비밀번호가 일치하지 않습니다"
                }
            }
        }
    }
}
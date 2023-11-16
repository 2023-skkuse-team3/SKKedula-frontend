package edu.skku.cs.skkedula

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import edu.skku.cs.skkedula.api.ApiObject
import edu.skku.cs.skkedula.api.Registration
import edu.skku.cs.skkedula.api.RegistrationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {
    private fun isValidId(id: String): Boolean {
        if (id.length < 4 || id.length > 10) return false
        return true
    }
    private fun isValidPw(password: String): Boolean {
        if (password.length < 8 || password.length > 12) return false
        return true
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

            val id = usernameText.text.toString()
            val name = "1001" // temp name
            val password1 = password1Text.text.toString()
            val password2 = password2Text.text.toString()

            // api interface 가져오기
            val reg = Registration(
                user_id = id,
                name = name,
                password = password1,
                confirmPassword = password2
            )
            val registerUser_ = ApiObject.service.registerUser(reg)

            // url post
            if (!isValidId(id)) {
                iderrorText.text = "사용할 수 없는 아이디입니다"
            }
            else if (!isValidPw(password1)) {
                pwerrorText.text = "사용할 수 없는 비밀번호입니다"
            }
            else {
                registerUser_.clone().enqueue(object : Callback<RegistrationResponse> {
                    override fun onResponse(
                        call: Call<RegistrationResponse>,
                        response: Response<RegistrationResponse>
                    ) {
                        val registrationResponse = response.body()
                        val statusmessage = registrationResponse?.message

                        if (response.isSuccessful.not()) {
                            // ID duplicated
                            if (response.code() == 500) {
                                iderrorText.text = "사용할 수 없는 아이디입니다"
                            }
                            // PW not match
                            else if (response.code() == 400) {
                                againpwerrorText.text = "비밀번호가 일치하지 않습니다"
                            }
                            Log.d("DATA", "id=${id}, pw=${password1}, pw2=${password2}")
                            Log.d("DATA", "code=${response.code()}, message = $statusmessage")
                            return
                        }
                        Log.d("DATA", "id=${id}, pw=${password1}, pw2=${password2}")
                        Log.d("DATA", "code=${response.code()}, message = $statusmessage")
                        Toast.makeText(
                            applicationContext,
                            "회원가입 성공! 로그인 화면으로 돌아가세요.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                        Log.e("ERROR", t.toString())
                        Toast.makeText(applicationContext, t.toString(), Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
    }
}
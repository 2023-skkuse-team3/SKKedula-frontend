package edu.skku.cs.skkedula

import android.app.Application
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import edu.skku.cs.skkedula.api.ApiObject
import edu.skku.cs.skkedula.api.Login
import edu.skku.cs.skkedula.api.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    class loginData: Application() {
        companion object {
            var userId: String = ""
        }
    }

    private fun changepg(username: String) {
        val intent = Intent(this, SkkedulaActivity::class.java)
        intent.putExtra("ID", username)
        startActivity(intent)
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

            // api interface 가져오기
            val log = Login(
                user_id = username,
                password = password
            )
            val loginUser_ = ApiObject.service.loginUser(log)

            // 다음 장 바로 넘어가기 위한 코드, 로그인 테스트 시 지우기
            //loginData.userId = username
            //changepg(username)

            // url post
            loginUser_.clone().enqueue(object: Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    val loginResponse = response.body()
                    val statusmessage = loginResponse?.message

                    if(response.isSuccessful.not()){
                        errorMessageTextView.text = "아이디나 비밀번호가 올바르지 않습니다."
                        errorMessageTextView.visibility = View.VISIBLE
                        Log.d("DATA", "code=${response.code()}, message = $statusmessage")
                        return
                    }

                    Log.d("DATA", "code=${response.code()}, message = $statusmessage")
                    errorMessageTextView.text = ""
                    finish()
                    loginData.userId = username
                    changepg(username)
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e("ERROR", t.toString())
                    Toast.makeText(applicationContext, t.toString(), Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}
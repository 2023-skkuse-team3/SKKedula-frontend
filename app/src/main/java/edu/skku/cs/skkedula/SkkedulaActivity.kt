package edu.skku.cs.skkedula

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.naver.maps.map.MapFragment
import edu.skku.cs.skkedula.databinding.ActivityMainBinding
import edu.skku.cs.skkedula.fragments.timetable.EmptyTimetable

class SkkedulaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var initialY: Float = 0f
    private var isCardHidden = false

    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("로그아웃 하시겠습니까?")

        builder.setPositiveButton("확인") { _, _ ->
            finish()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        builder.setNegativeButton("취소") { _, _ ->

        }
        val dialog = builder.create()
        dialog.show()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skkedula)

        // apply navigation bar
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.nav_view).setupWithNavController(navController)

        // logout control
        val logout = findViewById<ImageView>(R.id.imageView)
        logout.setOnClickListener{
            showLogoutDialog()
        }

        // set default timetable
        val emptyTimetable = EmptyTimetable()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainerView, emptyTimetable)
            .commit()

        // set card view
        val cardView = findViewById<FragmentContainerView>(R.id.card)
        val buttomDownAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_down)
        cardView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialY = event.y
                }

                MotionEvent.ACTION_MOVE -> {
                    val displacement = event.y - initialY
                    if (displacement > 0 && !isCardHidden) {
                        // Swipe down
                        val spring = SpringAnimation(
                            cardView,
                            DynamicAnimation.TRANSLATION_Y,
                            cardView.translationY
                        )

                        spring.addEndListener { _, _, _, _ ->
                            // Card is hidden
                            cardView.visibility = View.GONE
                        }
                        // 카드 내리기
                        cardView.startAnimation(buttomDownAnimation)
                        cardView.visibility = View.GONE
                    }
                }
            }
            true
        }
    }
}
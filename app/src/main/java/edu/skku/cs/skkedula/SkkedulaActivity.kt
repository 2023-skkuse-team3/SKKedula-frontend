package edu.skku.cs.skkedula

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.commit
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.skku.cs.skkedula.databinding.ActivityMainBinding

class SkkedulaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

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
    }
}
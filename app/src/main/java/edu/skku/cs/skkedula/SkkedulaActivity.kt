package edu.skku.cs.skkedula

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import android.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.skku.cs.skkedula.databinding.ActivityMainBinding

class SkkedulaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skkedula)

        // apply navigation bar
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.nav_view).setupWithNavController(navController)

        // TODO: Add action when logout button pressed
        val logout = findViewById<ImageView>(R.id.imageView)
        logout.setOnClickListener{
            Toast.makeText(this,"로그아웃 버튼 눌림", Toast.LENGTH_SHORT).show()
        }
    }
}
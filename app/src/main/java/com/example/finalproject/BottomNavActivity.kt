package com.example.finalproject

import android.os.Bundle
<<<<<<< Updated upstream
import com.google.android.material.bottomnavigation.BottomNavigationView
=======
>>>>>>> Stashed changes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_nav)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
<<<<<<< Updated upstream
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
=======
                R.id.navigation_diary,
                R.id.navigation_chat,
                R.id.navigation_mycourses,
                R.id.navigation_user,
                R.id.navigation_search
>>>>>>> Stashed changes
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}

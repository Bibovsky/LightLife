package com.example.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject.fragments.MyCoursesFragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
val fragment=MyCoursesFragment()
        supportFragmentManager.beginTransaction().add(R.id.fragment_container,fragment).commit()
    }
}

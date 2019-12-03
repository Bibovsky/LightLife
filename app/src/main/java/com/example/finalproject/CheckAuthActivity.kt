package com.example.finalproject

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CheckAuthActivity : AppCompatActivity() {
    lateinit var signUPLog: SharedPreferences
    lateinit var signUpPass: SharedPreferences
    lateinit var signInLogin: SharedPreferences
    lateinit var signInPass: SharedPreferences
    private lateinit var mDatebase: FirebaseDatabase
    private lateinit var mReference: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_auth)

        mDatebase = FirebaseDatabase.getInstance()
        mReference = mDatebase.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()
        signInLogin = getSharedPreferences("signInLoginPref", Context.MODE_PRIVATE)
        signInPass = getSharedPreferences("signInPasswordPref", Context.MODE_PRIVATE)
        signUPLog = getSharedPreferences("signUpLoginPref", Context.MODE_PRIVATE)
        signUpPass = getSharedPreferences("signUpPasswordPref", Context.MODE_PRIVATE)
        val signLogin = signInLogin.getString("signInLogin", null)
        val signPass = signInPass.getString("signInPassword", null)
        val signUpLogin = signUPLog.getString("signUpLoginPref", null)
        val signUpPassword = signUpPass.getString("signUpPasswordPref", null)
        if (signLogin != null && signPass != null) {
            mAuth.signInWithEmailAndPassword(signLogin!!, signPass!!)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        Toast.makeText(this, "Успешно", Toast.LENGTH_SHORT).show()

                        newActivity()
                    }
                }


        } else if (signUpLogin != null && signUpPassword != null) {
            mAuth.signInWithEmailAndPassword(signUpLogin!!, signUpPassword!!)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        Toast.makeText(this, "Успешно", Toast.LENGTH_SHORT).show()

                        newActivity()
                    }
                }
        } else {
            val intent = Intent(this, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }


    }

    fun newActivity() {
        val intent = Intent(this, BottomNavActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}

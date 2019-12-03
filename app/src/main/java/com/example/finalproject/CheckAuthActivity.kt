package com.example.finalproject

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CheckAuthActivity : AppCompatActivity() {
    lateinit var loginpref: SharedPreferences
    lateinit var passwordpref: SharedPreferences
    private lateinit var mDatebase: FirebaseDatabase
    private lateinit var mReference: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_auth)
        loginpref = getSharedPreferences("LOGINPREF", MODE_PRIVATE)
        passwordpref=getSharedPreferences("PASSWORDPREF",MODE_PRIVATE)
        mDatebase = FirebaseDatabase.getInstance()
        mReference = mDatebase.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()
        if (loginpref.getString("email", null)!=null && passwordpref.getString("password",null)!=null){
            val login= loginpref.getString("email",null)
            val password=passwordpref.getString("password",null)
            mAuth.signInWithEmailAndPassword(login!!,password!!).addOnCompleteListener { task->
                if (task.isSuccessful){
                    newActivity()
                }
            }
        }
        else{
            val intent=Intent(this,SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()        }
    }
    private fun newActivity() {
        val intent = Intent(this, BottomNavActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}

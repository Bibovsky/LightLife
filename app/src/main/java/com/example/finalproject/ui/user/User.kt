package com.example.finalproject.ui.user

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.finalproject.BottomNavActivity
import com.example.finalproject.R
import com.example.finalproject.SignInActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class User:Fragment() {
    lateinit var signInLogin:SharedPreferences
    lateinit var signInPassword:SharedPreferences
    lateinit var signInLoginEditor:SharedPreferences.Editor
    lateinit var signInPasswordEditor:SharedPreferences.Editor
    lateinit var signUpLogin:SharedPreferences
    lateinit var signUpPassword:SharedPreferences
    lateinit var signUpLoginEditor:SharedPreferences.Editor
    lateinit var signUpPasswordEditor:SharedPreferences.Editor
    private lateinit var mDatebase: FirebaseDatabase
    private lateinit var mReference: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view=LayoutInflater.from(container!!.context).inflate(R.layout.fragment_user,container,false)
        mDatebase = FirebaseDatabase.getInstance()
        mReference = mDatebase.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()
        val logout:Button=view.findViewById(R.id.logout)
        logout.setOnClickListener(){
            mAuth.signOut()
            signUpLogin=activity!!.getSharedPreferences("signUpLoginPref", MODE_PRIVATE)
            signUpPassword=activity!!.getSharedPreferences("signUpPasswordPref", MODE_PRIVATE)
            signInLogin=activity!!.getSharedPreferences("signInLoginPref",MODE_PRIVATE)
            signInPassword=activity!!.getSharedPreferences("signInPasswordPref",MODE_PRIVATE)
            signUpLoginEditor=signUpLogin.edit().clear()
            signUpLoginEditor.apply()
            signUpPasswordEditor=signUpPassword.edit().clear()
            signUpPasswordEditor.apply()
            signInLoginEditor=signInLogin.edit().clear()
            signInLoginEditor.apply()
            signInPasswordEditor=signInPassword.edit().clear()
            signInPasswordEditor.apply()
            val  intent=Intent(container.context,SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            activity!!.finish()
        }
        return view
    }
}
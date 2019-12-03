package com.example.finalproject.ui.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.finalproject.BottomNavActivity
import com.example.finalproject.R
import com.example.finalproject.SignInActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class User:Fragment() {
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
            val  intent=Intent(container.context,SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            activity!!.finish()
        }
        return view
    }
}
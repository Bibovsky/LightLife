package com.example.finalproject.ui.user

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.finalproject.AimRVAdapter
import com.example.finalproject.R
import com.example.finalproject.SignInActivity
import com.example.finalproject.WinsRVAdapter
import com.example.finalproject.models.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class User : Fragment() {
    lateinit var signInLogin: SharedPreferences
    lateinit var signInPassword: SharedPreferences
    lateinit var signInLoginEditor: SharedPreferences.Editor
    lateinit var signInPasswordEditor: SharedPreferences.Editor
    lateinit var signUpLogin: SharedPreferences
    lateinit var signUpPassword: SharedPreferences
    lateinit var signUpLoginEditor: SharedPreferences.Editor
    lateinit var signUpPasswordEditor: SharedPreferences.Editor
    private lateinit var mDatebase: FirebaseDatabase
    private lateinit var mReference: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private lateinit var aimsList: ArrayList<String>
    private var winsList: ArrayList<String> = arrayListOf()
    private lateinit var winsRV: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Constant.context = this
        aimsList = arrayListOf()
        for (i in 1..100) {
            aimsList.add(i.toString())
        }
        winsList = arrayListOf()

        val view = LayoutInflater.from(container!!.context)
            .inflate(R.layout.fragment_user, container, false)
        mDatebase = FirebaseDatabase.getInstance()
        mReference = mDatebase.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()
        val aimsRV: RecyclerView = view.findViewById(R.id.aim_rv)
        aimsRV.layoutManager =
            LinearLayoutManager(container.context, LinearLayoutManager.HORIZONTAL, false)
        aimsRV.adapter = AimRVAdapter(aimsList)
        winsRV = view.findViewById(R.id.wins_rv)
        winsRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        winsRV.adapter = WinsRVAdapter(winsList)
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(aimsRV)
        val logout: Button = view.findViewById(R.id.logout)
        logout.setOnClickListener() {
            mAuth.signOut()
            signUpLogin = activity!!.getSharedPreferences("signUpLoginPref", MODE_PRIVATE)
            signUpPassword = activity!!.getSharedPreferences("signUpPasswordPref", MODE_PRIVATE)
            signInLogin = activity!!.getSharedPreferences("signInLoginPref", MODE_PRIVATE)
            signInPassword = activity!!.getSharedPreferences("signInPasswordPref", MODE_PRIVATE)
            signUpLoginEditor = signUpLogin.edit().clear()
            signUpLoginEditor.apply()
            signUpPasswordEditor = signUpPassword.edit().clear()
            signUpPasswordEditor.apply()
            signInLoginEditor = signInLogin.edit().clear()
            signInLoginEditor.apply()
            signInPasswordEditor = signInPassword.edit().clear()
            signInPasswordEditor.apply()
            val intent = Intent(container.context, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            activity!!.finish()
        }
        return view
    }

    fun getRV(): RecyclerView {
        return winsRV
    }

    fun getWinsList(): ArrayList<String> {
        return winsList
    }
}
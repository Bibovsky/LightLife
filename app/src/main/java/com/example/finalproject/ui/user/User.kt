package com.example.finalproject.ui.user

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.bumptech.glide.Glide
import com.example.finalproject.AimRVAdapter
import com.example.finalproject.R
import com.example.finalproject.SignInActivity
import com.example.finalproject.WinsRVAdapter
import com.example.finalproject.models.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_bottom_nav.*

class User : Fragment() {
    lateinit var logout: Button
    private lateinit var mUser: FirebaseUser
    private lateinit var mStorage: FirebaseStorage
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
    lateinit var aimsRV: RecyclerView
    lateinit var usernameTV: TextView
    lateinit var userPhoto: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Constant.context = this
        initDB()

        val view = LayoutInflater.from(container!!.context)
            .inflate(R.layout.fragment_user, container, false)
        initLists()
        initViews(view)
        loadProfilePhoto()
        loadUserName()
        setListeners()
        aimsRV.layoutManager =
            LinearLayoutManager(container.context, LinearLayoutManager.HORIZONTAL, false)
        aimsRV.adapter = AimRVAdapter(aimsList)
        winsRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        winsRV.adapter = WinsRVAdapter(winsList)
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(aimsRV)
        mReference = mDatebase.reference.child("Users").child(mUser.uid).child("name")
        return view
    }
fun initLists(){
    aimsList = arrayListOf()
    for (i in 1..100) {
        aimsList.add(i.toString())
    }
    winsList = arrayListOf()
}
    fun setListeners() {
        logout.setOnClickListener() {
            mAuth.signOut()
            initSP()
            clearSP()
            val intent = Intent(container.context, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            activity!!.finish()
        }
    }

    fun loadUserName() {
        mDatebase.reference.child("Users").child(mUser.uid).child("name").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                usernameTV.text = p0.value.toString()
            }
        })
    }

    fun loadProfilePhoto() {
        val imgRef = mDatebase.reference.child("Users").child(mUser.uid).child("uri")
        imgRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                Glide.with(Constant.context).load(p0.value).centerCrop().circleCrop()
                    .into(userPhoto)
            }

        })
    }

    fun initSP() {
        signUpLogin = activity!!.getSharedPreferences("signUpLoginPref", MODE_PRIVATE)
        signUpPassword = activity!!.getSharedPreferences("signUpPasswordPref", MODE_PRIVATE)
        signInLogin = activity!!.getSharedPreferences("signInLoginPref", MODE_PRIVATE)
        signInPassword = activity!!.getSharedPreferences("signInPasswordPref", MODE_PRIVATE)
    }

    fun clearSP() {
        signUpLoginEditor = signUpLogin.edit().clear()
        signUpLoginEditor.apply()
        signUpPasswordEditor = signUpPassword.edit().clear()
        signUpPasswordEditor.apply()
        signInLoginEditor = signInLogin.edit().clear()
        signInLoginEditor.apply()
        signInPasswordEditor = signInPassword.edit().clear()
        signInPasswordEditor.apply()
    }

    fun initViews(view: View) {
        logout = view.findViewById(R.id.logout)
        usernameTV = view.findViewById<TextView>(R.id.user_name_tv)
        userPhoto = view.findViewById(R.id.user_photo)
        aimsRV = view.findViewById(R.id.aim_rv)
        winsRV = view.findViewById(R.id.wins_rv)

    }

    fun initDB() {
        mDatebase = FirebaseDatabase.getInstance()
        mReference = mDatebase.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()
        mUser = mAuth.currentUser!!
        mStorage = FirebaseStorage.getInstance()

        val user = mAuth.currentUser!!.uid
        val currentUserDb = mReference.child(user)
        val imgRef = currentUserDb.child("uri")


        Log.e("uri", imgRef.toString())


    }

    fun getRV(): RecyclerView {
        return winsRV
    }

    fun getWinsList(): ArrayList<String> {
        return winsList
    }

    override fun onDestroy() {
        super.onDestroy()
        val chronoText = Constant.chronotext
    }


}
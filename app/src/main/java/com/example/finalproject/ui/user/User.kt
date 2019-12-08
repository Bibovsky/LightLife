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
import com.example.finalproject.models.AimsList
import com.example.finalproject.models.Constant
import com.example.finalproject.models.WinsList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

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
    private lateinit var aimsList: ArrayList<AimsList>
    private var winsList: ArrayList<WinsList> = arrayListOf()
    private lateinit var winsRV: RecyclerView
    lateinit var aimsRV: RecyclerView
    lateinit var usernameTV: TextView
    lateinit var userPhoto: ImageView
    lateinit var aimName: TextView
    lateinit var aimTV: TextView
    lateinit var winTV: TextView
    lateinit var winName: TextView
    var winsCount = 0
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
        initLM()
        initAdapters()
        initAimsRV()
        initSnapHelpers()
        return view
    }

    fun initSnapHelpers() {
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(aimsRV)
        val snapHelperWins: SnapHelper = LinearSnapHelper()
        snapHelperWins.attachToRecyclerView(winsRV)
    }

    fun initLM() {
        aimsRV.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        winsRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    fun initAdapters() {
        aimsRV.adapter = AimRVAdapter(aimsList)
        winsRV.adapter = WinsRVAdapter(winsList)
    }

    fun initLists() {
        aimsList = arrayListOf()
        winsList = arrayListOf()
    }

    fun initAimsRV() {


        val aimref = mDatebase.reference.child("Users").child(mUser.uid).child("aims")

        aimref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e("FireBase", "Error")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.childrenCount.toInt() == 0) {
                    for (i in 1..100)
                        aimsList.add(AimsList(i.toString(), i.toString(), "00:00:00", false))
                    pushData()

                }
                for (child in p0.children) {
                    var aimName = child.child("name").value.toString()
                    var aimDesc = child.child("desc").value.toString()
                    val startTime = child.child("startTime").value.toString()
                    var isPlaying = child.child("isPlaying").value.toString().toBoolean()
                    aimsList.add(AimsList(aimName, aimDesc, startTime, isPlaying))


                }

                aimsRV.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                aimsRV.adapter = AimRVAdapter(aimsList)
                aimsPron(p0)
            }

        })
        val winRef = mDatebase.reference.child("Users").child(mUser.uid).child("wins")
        winRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                winsPron(p0.childrenCount)
            }

        })

    }

    fun pushData() {

        for (i in 0..aimsList.size - 1) {
            mDatebase.reference.child("Users").child(mUser.uid).child("aims").push()
                .setValue(
                    AimsList(
                        aimsList[i].name,
                        aimsList[i].desc,
                        aimsList[i].startAimTime,
                        aimsList[i].isPlaying
                    )
                )

        }


    }

    fun setListeners() {
        logout.setOnClickListener() {
            mAuth.signOut()
            initSP()
            clearSP()
            val intent = Intent(context, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            activity!!.finish()
        }
    }

    fun loadUserName() {
        mDatebase.reference.child("Users").child(mUser.uid).child("name")
            .addListenerForSingleValueEvent(object : ValueEventListener {
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
                Log.e("Firebase", "ERROR")
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
        winTV = view.findViewById(R.id.wins_tv)
        winName = view.findViewById(R.id.win_name_tv)
        aimName = view.findViewById(R.id.aim_name_tv)
        aimTV = view.findViewById(R.id.aims_tv)
        logout = view.findViewById(R.id.logout)
        usernameTV = view.findViewById(R.id.user_name_tv)
        userPhoto = view.findViewById(R.id.user_photo)
        aimsRV = view.findViewById(R.id.aim_rv)
        winsRV = view.findViewById(R.id.wins_rv)
    }

    fun initDB() {
        mDatebase = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        mUser = mAuth.currentUser!!
        mStorage = FirebaseStorage.getInstance()
        mReference = mDatebase.reference.child("Users").child(mUser.uid).child("name")
    }

    fun getAimsRv(): RecyclerView {
        return aimsRV
    }

    fun getWinsRv(): RecyclerView {
        return winsRV
    }

    fun getAimsList(): ArrayList<AimsList> {
        return aimsList
    }

    fun getWinsList(): ArrayList<WinsList> {
        return winsList
    }

    fun aimsPron(p0: DataSnapshot) {
        var aimPron = ""
        if (Constant.currentAims % 10 == 1) {
            aimPron = "Цель"
        } else if (Constant.currentAims % 10 == 11 || Constant.currentAims % 10 == 12 || Constant.currentAims % 10 == 13 || Constant.currentAims % 10 == 14) {
            aimPron = "Целей"
        } else if (Constant.currentAims % 10 >= 2 && Constant.currentAims <= 4) {
            aimPron = "Цели"
        } else aimPron = "Целей"
        aimTV.text = Constant.currentAims.toString()
        aimName.text = aimPron
    }

    fun winsPron(childrenCount: Long) {
        var winPron = ""
        if (childrenCount.toInt() % 10 == 1) {
            winPron = "Цель"
        } else if (childrenCount.toInt() % 10 == 11 || childrenCount.toInt() % 10 == 12 || childrenCount.toInt() % 10 == 13 ||childrenCount.toInt() % 10 == 14) {
            winPron = "Целей"
        } else if (childrenCount.toInt() % 10 >= 2 && childrenCount.toInt() <= 4) {
            winPron = "Цели"
        } else winPron = "Целей"
        winTV.text = winsCount.toString()
        winName.text = winPron
    }
}
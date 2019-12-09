package com.example.finalproject

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class SignUpActivity : AppCompatActivity() {
    var list = arrayListOf<String>()
    lateinit var userImageButton: ImageButton
    lateinit var signUpLoginPref: SharedPreferences
    lateinit var signUpPasswordPref: SharedPreferences
    lateinit var signUpLoginEditor: SharedPreferences.Editor
    lateinit var signUpPasswordEditor: SharedPreferences.Editor
    lateinit var signUpButton: Button
    lateinit var mDatebase: FirebaseDatabase
    lateinit var mReference: DatabaseReference
    lateinit var mAuth: FirebaseAuth
    lateinit var email: EditText
    lateinit var login: EditText
    lateinit var password: EditText
    lateinit var confPass: EditText
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setColorStatusBar()
        initSP()
        initializeViews()
        initDB()
        setListeners()
    }

    fun initSP() {
        signUpLoginPref = getSharedPreferences("signUpLoginPref", Context.MODE_PRIVATE)
        signUpPasswordPref = getSharedPreferences("signUpPasswordPref", Context.MODE_PRIVATE)
    }

    fun initDB() {
        mDatebase = FirebaseDatabase.getInstance()
        mReference = mDatebase.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()
    }

    fun initializeViews() {
        signUpButton = findViewById(R.id.complete_sign_up_button)
        email = findViewById(R.id.email_sign_up_et)
        login = findViewById(R.id.login_et)
        password = findViewById(R.id.password_sign_up_et)
        confPass = findViewById(R.id.password_confirm_et)
        userImageButton = findViewById(R.id.user_image_button)
    }

    fun setListeners() {
        signUpButton.setOnClickListener() {
            regNewUser()
        }
        userImageButton.setOnClickListener() {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data!!
            Log.e("data", data.toString())
            Glide.with(this).load(imageUri).centerCrop().circleCrop().into(userImageButton)
        }
    }

    private fun regNewUser() {
        val login = login.text.toString()
        val email = email.text.toString()
        val password = password.text.toString()
        val confirmPass = confPass.text.toString()
        if (email != "" && login != "" && password != "" && password == confirmPass) {
            mAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        val user = mAuth.currentUser!!.uid
                        val currentUserDb = mReference.child(user)
                        currentUserDb.child("name").setValue(login)
                        currentUserDb.child("email").setValue(email)
                        list = arrayListOf(email, password)
                        Log.e("LIST", list.toString())
                        fillSP(email, password)
                        Toast.makeText(this, "Успешно", Toast.LENGTH_SHORT).show()
                        if (imageUri != null) {
                            currentUserDb.child("user_photo")
                            uploadImageOnStorage()
                        } else {
                            imageUri =
                                Uri.parse("android.resource://com.example.finalproject/drawable/ic_launcher_stock")
                            uploadImageOnStorage()
                        }
                        newActivity()
                    } else Toast.makeText(
                        this,
                        "Неправильная почта или пароль",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } else {
            Toast.makeText(this, "Заполните поля", Toast.LENGTH_SHORT).show()
        }
    }

    fun fillSP(email: String, password: String) {
        signUpLoginEditor = signUpLoginPref.edit()
        signUpLoginEditor.putString("signUpLoginPref", email)
        signUpPasswordEditor = signUpPasswordPref.edit()
        signUpPasswordEditor.putString("signUpPasswordPref", password)
        signUpLoginEditor.apply()
        signUpPasswordEditor.apply()
    }

    private fun newActivity() {
        val intent = Intent(this, BottomNavActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()

    }

    private fun uploadImageOnStorage() {
        val filename = UUID.randomUUID().toString()
        val mRef = FirebaseStorage.getInstance().getReference("/images/$filename")
        mRef.putFile(imageUri!!).addOnCompleteListener {
            mRef.downloadUrl.addOnSuccessListener {
                Log.e("FirebaseStorage", "$it")
                val user=mAuth.currentUser!!.uid
                mReference.child(user).child("uri").setValue(it.toString())
            }
        }
    }

    private fun setColorStatusBar() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.StatusBar)
    }
}

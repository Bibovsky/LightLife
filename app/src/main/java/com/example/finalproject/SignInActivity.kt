package com.example.finalproject

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SignInActivity : AppCompatActivity() {
    lateinit var loginpref: SharedPreferences
    lateinit var passwordpref: SharedPreferences
    private lateinit var signInButton: Button
    private lateinit var mDatebase: FirebaseDatabase
    private lateinit var mReference: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private lateinit var signUpButton: Button
    private lateinit var email: EditText
    private lateinit var password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
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
        initializeViews()

        setListeners()


    }

    fun initializeViews() {
        signInButton = findViewById(R.id.sign_in_button)
        signUpButton = findViewById(R.id.sign_up_button)
        email = findViewById(R.id.email_sign_in_edit_text)
        password = findViewById(R.id.password_sign_in_edit_text)
    }

    fun setListeners() {
        signInButton.setOnClickListener() {
            auth()
        }
        signUpButton.setOnClickListener() {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun newActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    private fun auth() {
        val email = email.text.toString()
        val password = password.text.toString()
        if (email != "" && password != "") {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Успешно", Toast.LENGTH_SHORT).show()

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
}

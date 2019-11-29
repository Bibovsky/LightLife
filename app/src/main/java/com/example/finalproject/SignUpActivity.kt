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

class SignUpActivity : AppCompatActivity() {
    lateinit var loginEditor: SharedPreferences.Editor
    lateinit var passWordEditor:SharedPreferences.Editor
    lateinit var loginPref: SharedPreferences
    lateinit var passWordPref: SharedPreferences

    var list= arrayListOf<String>()
    private lateinit var signUpButton: Button
    private lateinit var mDatebase: FirebaseDatabase
    private lateinit var mReference: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private lateinit var email: EditText
    private lateinit var login: EditText
    private lateinit var password: EditText
    private lateinit var confPass: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        mDatebase = FirebaseDatabase.getInstance()
        mReference = mDatebase.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()
        loginPref = getSharedPreferences("LOGINPREF",MODE_PRIVATE)
        passWordPref=getSharedPreferences("PASSWORDPREF",MODE_PRIVATE)

        initializeViews()

        setListeners()

    }

    fun initializeViews() {
        signUpButton = findViewById(R.id.complete_sign_up_button)
        email = findViewById(R.id.email_sign_up_et)
        login = findViewById(R.id.login_et)
        password = findViewById(R.id.password_sign_up_et)
        confPass = findViewById(R.id.password_confirm_et)
    }

    fun setListeners() {
        signUpButton.setOnClickListener() {
            regNewUser()
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
                        list= arrayListOf(email,password)
                        Log.e("LIST",list.toString())
                        loginEditor = loginPref.edit()
                        passWordEditor = passWordPref.edit()

                        loginEditor.putString("email",email)
                        passWordEditor.putString("password",password)
                        loginEditor.apply()
                        passWordEditor.apply()
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

    private fun newActivity() {

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)

    }
}

package com.example.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.finalproject.models.DiaryModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class AddEventActivity : AppCompatActivity() {
    private lateinit var mDatebase: FirebaseDatabase
    private lateinit var mReference: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private lateinit var saveBtn:Button
    private lateinit var dateText:EditText
    private lateinit var timeText:EditText
    private lateinit var descText:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)

        mDatebase = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        mReference = mDatebase.reference.child("Users").child(mAuth.uid!!)

        saveBtn = findViewById(R.id.btnSave)
        dateText = findViewById(R.id.dateEdit)
        timeText = findViewById(R.id.timeEdit)
        descText = findViewById(R.id.descEdit)

        saveBtn.setOnClickListener {
            if(dateText.text.toString()!="" && timeText.text.toString()!="" && descText.text.toString()!="") {
                mReference.child("events").push().setValue(
                    DiaryModel(
                        dateText.text.toString(),
                        timeText.text.toString(),
                        descText.text.toString()
                    )
                )
                finish()
            }else{
                Toast.makeText(applicationContext,"Заполните все поля",Toast.LENGTH_SHORT)
            }

        }

        Log.e("ref",mAuth.currentUser!!.uid)

    }
}

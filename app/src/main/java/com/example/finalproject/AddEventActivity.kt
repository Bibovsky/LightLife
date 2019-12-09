package com.example.finalproject

import android.content.Context
import android.content.SharedPreferences
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.finalproject.models.DiaryModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.parser.PhoneNumberUnderscoreSlotsParser
import ru.tinkoff.decoro.watchers.FormatWatcher
import ru.tinkoff.decoro.watchers.MaskFormatWatcher


class AddEventActivity : AppCompatActivity() {
    private lateinit var mDatebase: FirebaseDatabase
    private lateinit var mReference: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private lateinit var saveBtn:Button
    lateinit var dateText:String
    private lateinit var timeText:EditText
    private lateinit var descText:EditText
    lateinit var datePref: SharedPreferences
    private var mTopToolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)

        mTopToolbar = findViewById(R.id.adEvToolbar)
        setSupportActionBar(mTopToolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        mTopToolbar!!.getNavigationIcon()!!.setColorFilter(getResources().getColor(R.color.F2F3FA), PorterDuff.Mode.SRC_ATOP)
        mTopToolbar!!.setNavigationOnClickListener {
            finish()
        }


        mDatebase = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        mReference = mDatebase.reference.child("Users").child(mAuth.uid!!)

        saveBtn = findViewById(R.id.btnSave)

        timeText = findViewById(R.id.timeEdit)
        descText = findViewById(R.id.descEdit)
        val slotsTime = PhoneNumberUnderscoreSlotsParser().parseSlots("__:__")

        val formatWatcherTime: FormatWatcher = MaskFormatWatcher(
            MaskImpl.createTerminated(slotsTime)
        )

        formatWatcherTime.installOn(timeText)
        datePref = getSharedPreferences("selectedDate", Context.MODE_PRIVATE)
        var dateText = datePref.getString("selectedDate","")


        saveBtn.setOnClickListener {
            if(dateText!="" && timeText.text.toString()!="" && descText.text.toString()!=""
                && dateText!!.length==10 &&  timeText.text.toString().length==5) {
                mReference.child("events").push().setValue(
                    DiaryModel(
                        dateText,
                        timeText.text.toString(),
                        descText.text.toString()
                    )
                )
                Toast.makeText(applicationContext,"Действие добавлено",Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(applicationContext,"Заполните все поля",Toast.LENGTH_SHORT).show()
            }

        }

        Log.e("ref",mAuth.currentUser!!.uid)

    }
}

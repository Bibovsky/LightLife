package com.example.finalproject.ui.diary

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.ui.activities.AddEventActivity
import com.example.finalproject.adapters.diary.DiaryAdapter
import com.example.finalproject.R
import com.example.finalproject.models.DiaryModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class Diary : Fragment() {
    private var mTopToolbar: Toolbar? = null
    private var diaryList = mutableListOf<DiaryModel>()
    private lateinit var mDatebase: FirebaseDatabase
    private lateinit var mReference: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    lateinit var datePref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = LayoutInflater.from(container!!.context)
            .inflate(R.layout.fragment_diary, container, false)
        datePref = activity!!.getSharedPreferences("selectedDate", Context.MODE_PRIVATE)
        var date = datePref.getString("selectedDate","")
        mTopToolbar = view.findViewById(R.id.my_toolbar)
        /*mTopToolbar!!.setNavigationIcon(R.drawable.backbtn)
        mTopToolbar!!.setNavigationOnClickListener {
            fragmentManager!!.popBackStack()
            fragmentManager!!.beginTransaction().replace(R.id.nav_host_fragment,DiaryCalendar()).commit()
        }

        mTopToolbar!!.getNavigationIcon()!!.setColorFilter(getResources().getColor(R.color.F2F3FA), PorterDuff.Mode.SRC_ATOP);*/
        //val currentTime = DateFormat.getDateTimeInstance().toString()
        //val fmt = SimpleDateFormat("dd.MM.yyyy")
        //val date = fmt.format(Date())

        mDatebase = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        mReference = mDatebase.reference.child("Users").child(mAuth.uid!!).child("events")

        (activity as AppCompatActivity?)!!.setSupportActionBar(mTopToolbar)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle("План дня на $date")

        val diaryRec = view.findViewById<RecyclerView>(R.id.diaryRecycler)
        diaryRec.layoutManager = LinearLayoutManager(context)
        mReference.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                diaryList.clear()
                for(i in p0.children){
                    if (i.child("date").value.toString() == date){
                        var mDate=i.child("date").value.toString()
                        var mTime=i.child("time").value.toString()
                        var mDesc=i.child("action").value.toString()

                        diaryList.add(DiaryModel(mDate, mTime, mDesc))
                    }
                    diaryList.sortBy { it.time }
                    diaryRec.adapter =
                        DiaryAdapter(
                            diaryList
                        )
                    Log.e("firebase",i.child("date").value.toString())
                }

            }


        });


        //diaryList.add(DiaryModel(date, "08:00", "Завтрак"))







        val fab: View = view.findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            val intent = Intent(context, AddEventActivity::class.java)
            startActivity(intent)
        }
        return view
    }
}
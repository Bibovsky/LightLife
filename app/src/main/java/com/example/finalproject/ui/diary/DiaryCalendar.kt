package com.example.finalproject.ui.diary


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.AddEventActivity
import com.example.finalproject.BottomNavActivity
import com.example.finalproject.DiaryAdapter
import com.example.finalproject.R
import com.example.finalproject.models.DiaryModel
import com.google.firebase.FirebaseError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import java.text.SimpleDateFormat
import java.util.*


class DiaryCalendar : Fragment() {

    private lateinit var calendar:MaterialCalendarView
    lateinit var datePref: SharedPreferences
    lateinit var datePrefEditor: SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = LayoutInflater.from(container!!.context)
            .inflate(R.layout.fragment_calendar, container, false)
        calendar = view.findViewById(R.id.calendarView)
        datePref = activity!!.getSharedPreferences("selectedDate", Context.MODE_PRIVATE)
        datePrefEditor = datePref.edit()
        calendar.setOnDateChangedListener(object:OnDateSelectedListener{
            override fun onDateSelected(
                widget: MaterialCalendarView,
                date: CalendarDay,
                selected: Boolean
            ) {

                var sDate = "${addZero(date.day.toString())}.${addZero(date.month.toString())}.${date.year}"
                Log.e("calendar",sDate)


                datePrefEditor.putString("selectedDate",sDate)
                datePrefEditor.apply()

                //(activity as BottomNavActivity).fragmentrepl()

                fragmentManager!!.beginTransaction().replace(R.id.nav_host_fragment,Diary()).addToBackStack(null).commit()



            }
        })

        return view
    }
}
fun addZero(a:String):String{
    if (a.length==1) return "0$a"
    else return a

}
package com.example.finalproject.ui.diary

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.finalproject.DiaryAdapter
import com.example.finalproject.MyCoursesAdapter
import com.example.finalproject.R
import com.example.finalproject.models.DiaryModel
import com.example.finalproject.models.MyCoursesModel
import com.google.android.material.snackbar.Snackbar
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class Diary:Fragment() {
    private var mTopToolbar: Toolbar? = null
    private var diaryList = mutableListOf<DiaryModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=LayoutInflater.from(container!!.context).inflate(R.layout.fragment_diary,container,false)

        mTopToolbar =view.findViewById(R.id.my_toolbar);

        //val currentTime = DateFormat.getDateTimeInstance().toString()
        val fmt = SimpleDateFormat("dd.MM.yyyy")
        val date = fmt.format(Date())


        (activity as AppCompatActivity?)!!.setSupportActionBar(mTopToolbar)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle("План дня на $date")
        for (i in 1..30) {
            diaryList.add(DiaryModel("08:00","Завтрак"))

        }
        Log.e("diaryList",diaryList.size.toString())
        val diaryRec = view.findViewById<RecyclerView>(R.id.diaryRecycler)
        diaryRec.layoutManager = LinearLayoutManager(context)
        diaryRec.adapter = DiaryAdapter(diaryList)

        val fab: View = view.findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show()
        }
        return view
    }
}
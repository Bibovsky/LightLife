package com.example.finalproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.finalproject.MyCoursesAdapter
import com.example.finalproject.R
import com.example.finalproject.models.MyCoursesModel

class MyCoursesFragment() : Fragment() {
    private var myCoursesList = mutableListOf<MyCoursesModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        for (i in 1..100) {
            myCoursesList.add(MyCoursesModel("Photo $i", "course $i", "description $i"))
        }
        val view =
            LayoutInflater.from(context).inflate(R.layout.my_courses_fragment, container, false)
        val myCoursesRV = view.findViewById<RecyclerView>(R.id.my_courses_rv)
        val continueCourseButton:Button=view.findViewById(R.id.continue_course_button)
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(myCoursesRV)
        myCoursesRV.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        myCoursesRV.adapter = MyCoursesAdapter(myCoursesList)
        return view

    }
}
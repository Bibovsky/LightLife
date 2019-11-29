package com.example.finalproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.models.MyCoursesModel

class MyCoursesAdapter(val myCoursesList: MutableList<MyCoursesModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyCoursesViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.my_courses_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return myCoursesList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MyCoursesViewHolder).bind(position)
    }

    inner class MyCoursesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val myCoursesPhoto: TextView = view.findViewById(R.id.my_courses_photo)
        val myCoursesName:TextView=view.findViewById(R.id.my_courses_name_text_view)
        val myCoursesDescription:TextView=view.findViewById(R.id.my_courses_description_text_view)
        fun bind(position: Int) {
            myCoursesPhoto.text = myCoursesList[position].photo
            myCoursesName.text=myCoursesList[position].courseName
            myCoursesDescription.text=myCoursesList[position].courseDescription
        }
    }
}
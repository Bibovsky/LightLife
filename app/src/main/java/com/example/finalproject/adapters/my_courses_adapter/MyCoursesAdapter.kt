package com.example.finalproject.adapters.my_courses_adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.example.finalproject.models.PreviewCourseModel
import com.example.finalproject.ui.passingcourse.PassCourseActivity
import jp.wasabeef.glide.transformations.BlurTransformation
import com.example.finalproject.R
import com.example.finalproject.models.MyCoursesModel

class MyCoursesAdapter(val list: ArrayList<PreviewCourseModel>, val context: Context) :
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
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MyCoursesViewHolder).bind(position)
        holder.setListener()
    }

    inner class MyCoursesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val coursesTitle: TextView = view.findViewById(R.id.my_courses_name_text_view)
        private val imageView: ImageView = view.findViewById(R.id.my_courses_photo)
        private val description: TextView = view.findViewById(R.id.my_courses_description)
        private val numbersPeople: TextView = view.findViewById(R.id.my_course_number_people)
        private val resumeBtn: Button = view.findViewById(R.id.continue_button)

        fun setListener(){
            resumeBtn.setOnClickListener{
                val intent = Intent(context, PassCourseActivity::class.java)
                intent.putExtra("ID_COURSE", list[position].id)
                intent.putExtra("TITLE_COURSE", list[position].title)
                ContextCompat.startActivity(context, intent, bundleOf())
            }
        }

        fun bind(position: Int) {
            coursesTitle.text = list[position].title
            description.text = list[position].description
            numbersPeople.text = list[position].numberPeople.toString()
            Glide.with(context)
                .load(list[position].imageUrl)
                .into(imageView)
        }




    }
}
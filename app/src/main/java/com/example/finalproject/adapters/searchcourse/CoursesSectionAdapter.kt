package com.example.finalproject.adapters.searchcourse

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.models.PreviewCourseData
import com.example.finalproject.ui.detailcourse.CourseShowDetail
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters

class CoursesSectionAdapter(
    private val title: String,
    private val list: List<PreviewCourseData>,
    private val context: Context
) : Section(
    SectionParameters
        .builder()
        .itemResourceId(R.layout.course_item)
        .headerResourceId(R.layout.section_header)
        .build()
) {

    override fun getContentItemsTotal(): Int {
        return list.size
    }


    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val itemHolder = holder as ItemViewHolder
        val course = list[position]
        itemHolder.titleCourse.text = course.title
        itemHolder.description.text = course.description
        itemHolder.rating.text = course.rating.toString()
        itemHolder.numberPeople.text = course.numberPeople.toString()
        Glide.with(context).load(course.imageUrl).into(itemHolder.image)

        holder.itemView.setOnClickListener {
            startShowDetailCourse(course.id)
        }

    }


    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder) {
        val headerHolder = holder as HeaderViewHolder
        headerHolder.titleHeader.text = title
    }


    override fun getItemViewHolder(view: View): RecyclerView.ViewHolder? {
        return ItemViewHolder(view)
    }


    override fun getHeaderViewHolder(view: View): RecyclerView.ViewHolder {
        return HeaderViewHolder(view)
    }


    inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleHeader: TextView = view.findViewById(R.id.tvTitle)
    }


    inner class ItemViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {
        val titleCourse: TextView = rootView.findViewById(R.id.title_course)
        val description: TextView = rootView.findViewById(R.id.description)
        val rating: TextView = rootView.findViewById(R.id.rating)
        val numberPeople: TextView = rootView.findViewById(R.id.users_number)
        val image: ImageView = rootView.findViewById(R.id.imageIconCourse)
    }


    private fun startShowDetailCourse(id: String) {
        val intent = Intent(context, CourseShowDetail::class.java)
        intent.putExtra("ID", id)
        startActivity(context, intent, bundleOf())
    }
}

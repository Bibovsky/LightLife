package com.example.finalproject.adapters.searchcourse


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.models.PreviewCourseModel
import com.google.firebase.database.*
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import java.util.ArrayList
import java.util.LinkedHashMap


class CategoriesAdapter(private val list: List<String>, val context: Context, private val coursesRecyclerView: RecyclerView) :
    RecyclerView.Adapter<CategoriesAdapter.ItemPostHolder>() {

    private val CHILD: String = "CourseData"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemPostHolder {
        return ItemPostHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.categories_item, parent, false)
        )
    }


    override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(holder: ItemPostHolder, position: Int) {
        val user = list[position]
        holder.bind(user)

        holder.itemView.setOnClickListener {
            setListCourse(list[position])
        }
    }


    class ItemPostHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleCourse = itemView.findViewById<TextView>(R.id.title_course)
        fun bind(post: String) {
            titleCourse.text = post
        }
    }


    private fun setListCourse(headerSection: String) {
        val list: ArrayList<PreviewCourseModel> = arrayListOf()
        val ref: DatabaseReference = FirebaseDatabase.getInstance().reference.child(CHILD)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    val dataCourseModel: PreviewCourseModel? =
                        it.getValue(PreviewCourseModel::class.java)
                    list.add(
                        PreviewCourseModel(
                            it.key!!,
                            dataCourseModel!!.title,
                            dataCourseModel.description,
                            dataCourseModel.nameSection,
                            dataCourseModel.rating,
                            dataCourseModel.numberPeople,
                            dataCourseModel.imageUrl
                        )
                    )
                }
                createSectionCoursesRecycler(list, headerSection)
            }

            override fun onCancelled(p0: DatabaseError) {
                //not implemented
            }
        })
    }


    private fun createSectionCoursesRecycler(list: ArrayList<PreviewCourseModel>, headerSection: String) {
        val sectionedAdapter = SectionedRecyclerViewAdapter()
        val map = LinkedHashMap<String, ArrayList<PreviewCourseModel>>()

        val filteredCourses = getCoursesOneSection(list, headerSection)
        map[headerSection] = filteredCourses

        for (entry in map.entries) {
            if (entry.value.isNotEmpty()) {
                sectionedAdapter.addSection(
                    CoursesSectionAdapter(
                        entry.key,
                        entry.value,
                        context!!
                    )
                )
            }
        }

        initCoursesRecycler(sectionedAdapter)
    }


    private fun initCoursesRecycler(sectionedAdapter: SectionedRecyclerViewAdapter) {
        coursesRecyclerView.layoutManager = LinearLayoutManager(context)
        coursesRecyclerView.adapter = sectionedAdapter
    }


    private fun getCoursesOneSection(list: ArrayList<PreviewCourseModel>, nameSection: String): ArrayList<PreviewCourseModel> {
        val listOneSection: ArrayList<PreviewCourseModel> = arrayListOf()
        val strIterable: Iterator<PreviewCourseModel> = list.iterator()

        while (strIterable.hasNext()) {
            val value = strIterable.next()
            if (value.nameSection == nameSection) {
                listOneSection.add(value)
            }
        }
        return listOneSection
    }

}




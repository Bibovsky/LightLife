package com.example.finalproject.ui.search

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.adapters.searchcourse.CategoriesAdapter
import com.example.finalproject.adapters.searchcourse.CoursesSectionAdapter
import com.example.finalproject.models.DetailCourseData
import com.example.finalproject.models.PreviewCourseData
import com.google.firebase.database.*
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import java.util.*


class SearchFragment : Fragment() {

    private lateinit var mReference: DatabaseReference
    private lateinit var coursesRecyclerView: RecyclerView
    private lateinit var categoriesRecyclerView: RecyclerView
    private lateinit var searchEditText: EditText
    private val listHeaderSections: ArrayList<String> = arrayListOf("Питание", "Спорт", "Режим дня")
    private val CHILD: String = "CourseData"
    private val CHILD_IMAGES: String = "images_courses/"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = LayoutInflater.from(container!!.context)
            .inflate(R.layout.fragment_search, container, false)

        mReference = FirebaseDatabase.getInstance().reference.child(CHILD)

/*
        sendDemoData(
            "Как начать?",
            " И это неудивительно. При всем обилии информации, нет единого мнения или общепринятой системы, которая была бы хороша для каждого. Поэтому запутаться в вопросах питания очень легко. Многие люди из-за этого испытывают разочарование. Диеты не работают. Советы из книг или Интернета тоже. Все это знакомо мне не понаслышке. Я сама была жертвой диет, неправильно худела, не могла набрать мышечную массу, неправильно питалась."
                    +
                    "Мною были потрачены годы на изучение информации, эксперименты с питанием, наблюдение за своими клиентами, чтобы понять, как все это на самом деле просто. Данный курс это собрание самых важных и ценных знаний о питании в сжатой и доступной форме.", "Спорт", 6.7, 553, "https://firebasestorage.googleapis.com/v0/b/finalproject-757d3.appspot.com/o/courses_images%2F99f4f4792eb2331223c29ad715fd1931.jpg?alt=media&token=9f8c089d-af1e-40c0-8fea-0caa8280757a", "Русский", 5
        )
*/

        initializeViews(view)
        setListenerSearchViaEditText()
        initCategoriesRecycler()
        setListCourse(false, "")

        return view
    }


    private fun setListenerSearchViaEditText() {
        searchEditText.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val textSearch = searchEditText.text.toString().trim().toLowerCase()
                setListCourse(true, textSearch)

                val imm = v.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                return@OnEditorActionListener true
            }
            false
        })
    }


    private fun filteredSearchedCourses(
        list: ArrayList<PreviewCourseData>,
        titleForSearch: String
    ): ArrayList<PreviewCourseData> {
        val resultList: ArrayList<PreviewCourseData> = arrayListOf()
        val strIterable: Iterator<PreviewCourseData> = list.iterator()

        while (strIterable.hasNext()) {
            val value = strIterable.next()
            if (value.title.trim().toLowerCase() == titleForSearch || titleForSearch == "") {
                resultList.add(value)
            }
        }
        return resultList
    }


    private fun setListCourse(isSearch: Boolean, titleForSearch: String) {
        var list: ArrayList<PreviewCourseData> = arrayListOf()
        val ref: DatabaseReference = FirebaseDatabase.getInstance().reference.child(CHILD)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    val dataCourseData: PreviewCourseData? =
                        it.getValue(PreviewCourseData::class.java)
                    list.add(
                        PreviewCourseData(
                            it.key!!,
                            dataCourseData!!.title,
                            dataCourseData.description,
                            dataCourseData.nameSection,
                            dataCourseData.rating,
                            dataCourseData.numberPeople,
                            dataCourseData.imageUrl
                        )
                    )
                }
                if (isSearch) {
                    list = filteredSearchedCourses(list, titleForSearch)
                }
                createSectionCoursesRecycler(list)
            }

            override fun onCancelled(p0: DatabaseError) {
                //not implemented
            }
        })
    }


    private fun createSectionCoursesRecycler(list: ArrayList<PreviewCourseData>) {
        val sectionedAdapter = SectionedRecyclerViewAdapter()
        val map = LinkedHashMap<String, ArrayList<PreviewCourseData>>()

        for (i in 0 until listHeaderSections.size) {
            val filteredCourses = getCoursesOneSection(list, listHeaderSections[i])
            map[listHeaderSections[i]] = filteredCourses
        }

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


    private fun getCoursesOneSection(
        list: ArrayList<PreviewCourseData>,
        nameSection: String
    ): ArrayList<PreviewCourseData> {
        val listOneSection: ArrayList<PreviewCourseData> = arrayListOf()
        val strIterable: Iterator<PreviewCourseData> = list.iterator()

        while (strIterable.hasNext()) {
            val value = strIterable.next()
            if (value.nameSection == nameSection) {
                listOneSection.add(value)
            }
        }
        return listOneSection
    }


    private fun initCoursesRecycler(sectionedAdapter: SectionedRecyclerViewAdapter) {
        coursesRecyclerView.layoutManager = LinearLayoutManager(context)
        coursesRecyclerView.adapter = sectionedAdapter
    }


    private fun initCategoriesRecycler() {
        categoriesRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        categoriesRecyclerView.adapter =
            CategoriesAdapter(
                listHeaderSections,
                context!!,
                coursesRecyclerView
            )
    }


    private fun initializeViews(view: View) {
        searchEditText = view.findViewById(R.id.searchEditText)
        coursesRecyclerView = view.findViewById(R.id.coursesRecycler)
        categoriesRecyclerView = view.findViewById(R.id.categoriesRecycler)
    }


    private fun sendDemoData(
        title: String,
        description: String,
        nameSection: String,
        rating: Double,
        numberPeople: Int,
        imageUrl: String,
        lang: String,
        hoursForPass: Int
    ) {
        val data = DetailCourseData(
            title,
            description,
            nameSection,
            rating,
            numberPeople,
            imageUrl,
            lang,
            hoursForPass
        )
        mReference.push().setValue(data)
    }


}


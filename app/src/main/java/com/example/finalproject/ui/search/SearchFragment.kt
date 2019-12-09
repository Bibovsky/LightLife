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
import com.example.finalproject.models.*
import com.example.finalproject.models.Constant.Companion.context
import com.google.android.youtube.player.internal.i
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import kotlinx.android.synthetic.main.video_item.*
import java.util.*
import kotlin.collections.ArrayList


class SearchFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mReference: DatabaseReference
    private lateinit var coursesRecyclerView: RecyclerView
    private lateinit var categoriesRecyclerView: RecyclerView
    private lateinit var searchEditText: EditText
    private val listHeaderSections: ArrayList<String> = arrayListOf("Питание", "Спорт", "Режим дня")
    private val CHILD: String = "CourseData"
    private val CHILD_MY_COURSES = "MyCourses"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = LayoutInflater.from(container!!.context)
            .inflate(R.layout.fragment_search, container, false)

        mReference = FirebaseDatabase.getInstance().reference.child(CHILD)

        initializeViews(view)
        setListenerSearchViaEditText()
        initCategoriesRecycler()
        setListCourse(false, "")

        return view
    }


    private fun setListenerSearchViaEditText() {
        //for search via EditText field
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
        list: ArrayList<PreviewCourseModel>,
        titleForSearch: String
    ): ArrayList<PreviewCourseModel> {
        val resultList: ArrayList<PreviewCourseModel> = arrayListOf()
        val strIterable: Iterator<PreviewCourseModel> = list.iterator()

        while (strIterable.hasNext()) {
            val value = strIterable.next()
            if (value.title.trim().toLowerCase() == titleForSearch || titleForSearch == "") {
                resultList.add(value)
            }
        }
        return resultList
    }


    private fun setListCourse(isSearch: Boolean, titleForSearch: String) {
        var list: ArrayList<PreviewCourseModel> = arrayListOf()
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
                if (isSearch) {
                    list = filteredSearchedCourses(list, titleForSearch)
                }
                //delete courses that have already started by user
                deleteSelectedCourse(list)
            }

            override fun onCancelled(p0: DatabaseError) {
                //not implemented
            }
        })
    }


    private fun createSectionCoursesRecycler(list: ArrayList<PreviewCourseModel>) {
        val sectionedAdapter = SectionedRecyclerViewAdapter()
        val map = LinkedHashMap<String, ArrayList<PreviewCourseModel>>()

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
        list: ArrayList<PreviewCourseModel>,
        nameSection: String
    ): ArrayList<PreviewCourseModel> {
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


    private fun deleteSelectedCourse(list: ArrayList<PreviewCourseModel>) {
        mAuth = FirebaseAuth.getInstance()
        val resultList: ArrayList<PreviewCourseModel> = ArrayList(list)

        val ref: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child(CHILD_MY_COURSES).child(mAuth.uid!!)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    val id: String = it.value.toString()

                    for (i in 0 until resultList.size){
                        if(id == resultList[i].id){
                            list.remove(resultList[i])
                        }
                    }
                }

                //creating RecyclerView with sections
                createSectionCoursesRecycler(list)
            }

            override fun onCancelled(p0: DatabaseError) {
                //not implemented
            }
        })
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


    //DEMO TEST !!!
    private fun sendDemoData(
        title: String,
        description: String,
        nameSection: String,
        rating: Double,
        numberPeople: Int,
        imageUrl: String,
        lang: String,
        hoursForPass: Int,
        dataVideo: ArrayList<VideoModel>,
        dataText: ArrayList<TextPassCourseModel>
    ) {
        val data = DemoData(
            title,
            description,
            nameSection,
            rating,
            numberPeople,
            imageUrl,
            lang,
            hoursForPass,
            dataVideo,
            dataText
        )
        mReference.push().setValue(data)
    }

}


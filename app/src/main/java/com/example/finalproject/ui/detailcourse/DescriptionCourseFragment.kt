package com.example.finalproject.ui.detailcourse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.finalproject.R
import com.example.finalproject.models.DetailCourseData
import com.example.finalproject.models.PreviewCourseData
import kotlinx.android.synthetic.main.course_item.*

class DescriptionCourseFragment(private val data: DetailCourseData) : Fragment() {

    private lateinit var timeToPass: TextView
    private lateinit var languages: TextView
    private lateinit var description: TextView
    private val HOURS: String = " часов"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_detail_description_course, container, false)!!
        initializeViews(view)
        setData(data)

        return view
    }


    private fun initializeViews(view: View){
        description = view.findViewById(R.id.descriptionDetailCourse)
        timeToPass = view.findViewById(R.id.timeToPass)
        languages = view.findViewById(R.id.languages)
    }


    private fun setData(data: DetailCourseData){
        description.text = data.description
        timeToPass.text = (data.hoursForPass.toString() + HOURS)
        languages.text = data.lang
    }


}

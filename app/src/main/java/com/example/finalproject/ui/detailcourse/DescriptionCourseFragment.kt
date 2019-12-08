package com.example.finalproject.ui.detailcourse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.finalproject.R
import com.example.finalproject.models.DetailCourseModel

class DescriptionCourseFragment(private val model: DetailCourseModel) : Fragment() {

    private lateinit var timeToPass: TextView
    private lateinit var languages: TextView
    private lateinit var description: TextView
    private val HOURS: String = " часов"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View =
            inflater.inflate(R.layout.fragment_detail_description_course, container, false)!!
        initializeViews(view)
        setData(model)

        return view
    }


    private fun initializeViews(view: View) {
        description = view.findViewById(R.id.descriptionDetailCourse)
        timeToPass = view.findViewById(R.id.timeToPass)
        languages = view.findViewById(R.id.languages)
    }


    private fun setData(model: DetailCourseModel) {
        description.text = model.description
        timeToPass.text = (model.hoursForPass.toString() + HOURS)
        languages.text = model.lang
    }


}

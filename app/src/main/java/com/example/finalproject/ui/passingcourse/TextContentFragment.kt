package com.example.finalproject.ui.passingcourse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.finalproject.R
import com.example.finalproject.models.TextPassCourseModel
import com.google.firebase.database.*
import jp.wasabeef.glide.transformations.BlurTransformation


class TextContentFragment(val idCourse: String) : Fragment() {

    private val CHILD: String = "CourseData"
    private val CHILD_TEXT = "dataTextPassCourse"
    private lateinit var nextBtn: Button
    private lateinit var prevBtn: Button
    private lateinit var textContent: TextView
    private lateinit var imageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_passing_course_text, container, false)!!


        getDataForPassCourse(idCourse, view)
        return view
    }


    private fun getDataForPassCourse(idCourse: String, view: View) {
        val list: ArrayList<TextPassCourseModel> = arrayListOf()
        val reference: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child(CHILD).child(idCourse).child(CHILD_TEXT)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    val model: TextPassCourseModel? =
                        it.getValue(TextPassCourseModel::class.java)
                    list.add(
                        TextPassCourseModel(
                            model!!.textContent,
                            model.urlImg
                        )
                    )
                }
                initViews(view)
                setListeners(list)
            }

            override fun onCancelled(p0: DatabaseError) {
                //not implemented
            }
        })
    }


    private fun setListeners(list: ArrayList<TextPassCourseModel>) {
        //it doesn't look good
        var nextFlag = false
        var prevFlag = true
        var value: TextPassCourseModel
        val strIterable: ListIterator<TextPassCourseModel> = list.listIterator()

        value = strIterable.next()
        textContent.text = value.textContent
        Glide.with(this)
            .load(value.urlImg)
            .into(imageView)

        prevBtn.visibility = View.GONE

        nextBtn.setOnClickListener {
            prevFlag = true
            if (strIterable.hasNext()) {
                value = strIterable.next()

                if(strIterable.hasNext() && nextFlag){
                    nextFlag = false
                    value = strIterable.next()
                }
                textContent.text = value.textContent
                Glide.with(this)
                    .load(value.urlImg)
                    .into(imageView)

                changeVisibilityButton(strIterable)
            }
        }

        prevBtn.setOnClickListener {
            if (strIterable.hasPrevious()) {
                nextFlag = true
                value = strIterable.previous()
                if(strIterable.hasPrevious() && prevFlag){
                    prevFlag = false
                    value = strIterable.previous()
                }
                textContent.text = value.textContent
                Glide.with(this)
                    .load(value.urlImg)
                    .into(imageView)

                changeVisibilityButton(strIterable)
            }
        }
    }


    private fun initViews(view: View) {
        nextBtn = view.findViewById(R.id.next)
        prevBtn = view.findViewById(R.id.prev)
        textContent = view.findViewById(R.id.passCourseTextContent)
        imageView = view.findViewById(R.id.passingCourseImage)

        nextBtn.visibility = View.VISIBLE
        prevBtn.visibility = View.VISIBLE
    }


    private fun changeVisibilityButton(strIterable: ListIterator<TextPassCourseModel>) {
        if (strIterable.hasPrevious()) {
            prevBtn.visibility = View.VISIBLE
        } else {
            prevBtn.visibility = View.GONE
        }

        if (strIterable.hasNext()) {
            nextBtn.visibility = View.VISIBLE
        } else {
            nextBtn.visibility = View.GONE
        }
    }
}
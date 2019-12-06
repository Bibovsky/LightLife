package com.example.finalproject.ui.detailcourse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.adapters.detailcourse.ReviewCourseAdapter
import com.example.finalproject.models.DetailCourseData
import com.example.finalproject.models.ReviewModel
import com.google.firebase.database.*

class ReviewCourseFragment : Fragment() {

    private lateinit var reviewsRecyclerView: RecyclerView
    private val CHILD: String = "CourseData"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_detail_review_course, container, false)!!

        initRecyclerReviews(view)
        return view
    }




    private fun initRecyclerReviews(view: View) {
        var list: ArrayList<ReviewModel> = arrayListOf()
        for (i in 0..12) {
            list.add(
                ReviewModel(
                    "Иван Иванов",
                    "Отличный курс, очень помог прийти в форму, ставлю лайк класс нраица"
                )
            )
        }

        reviewsRecyclerView = view.findViewById(R.id.reviewsRecycler)
        reviewsRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        reviewsRecyclerView.adapter =
            ReviewCourseAdapter(list, context!!)
    }


    private fun loadReviews(id: String) {
        val reference: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child(CHILD).child(id)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val data: DetailCourseData? = p0.getValue(DetailCourseData::class.java)
                TODO()
            }

            override fun onCancelled(p0: DatabaseError) {
                //not implemented
            }
        })
    }


}


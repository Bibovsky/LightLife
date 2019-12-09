package com.example.finalproject.ui.mycourses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.finalproject.R
import com.example.finalproject.adapters.my_courses_adapter.MyCoursesAdapter
import com.example.finalproject.models.PreviewCourseModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MyCourses : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private val CHILD: String = "CourseData"
    private val CHILD_MY_COURSES = "MyCourses"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            LayoutInflater.from(context).inflate(R.layout.my_courses_fragment, container, false)

        getMyCourses(view)

        return view
    }


    private fun getMyCourses(view: View) {
        val myCoursesId: ArrayList<String> = arrayListOf()
        mAuth = FirebaseAuth.getInstance()
        val ref: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child(CHILD_MY_COURSES).child(mAuth.uid!!)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    myCoursesId.add(it.value.toString())
                }
                getContentMyCourses(myCoursesId, view)
            }

            override fun onCancelled(p0: DatabaseError) {
                //not implemented
            }
        })
    }


    private fun getContentMyCourses(myCoursesId: ArrayList<String>, view: View) {
        val list: ArrayList<PreviewCourseModel> = arrayListOf()
        val ref: DatabaseReference = FirebaseDatabase.getInstance().reference.child(CHILD)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (i in 0 until myCoursesId.size) {
                    val dataCourseModel =
                        p0.child(myCoursesId[i]).getValue(PreviewCourseModel::class.java)
                    list.add(
                        PreviewCourseModel(
                            p0.child(myCoursesId[i]).key!!,
                            dataCourseModel!!.title,
                            dataCourseModel.description,
                            dataCourseModel.nameSection,
                            dataCourseModel.rating,
                            dataCourseModel.numberPeople,
                            dataCourseModel.imageUrl
                        )
                    )
                }
                initRecyclerView(list, view)
            }

            override fun onCancelled(p0: DatabaseError) {
                //not implemented
            }
        })
    }


    private fun initRecyclerView(list: ArrayList<PreviewCourseModel>, view: View) {
        val myCoursesRV = view.findViewById<RecyclerView>(R.id.my_courses_rv)
        val snapHelper: SnapHelper = LinearSnapHelper()

        snapHelper.attachToRecyclerView(myCoursesRV)
        myCoursesRV.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        myCoursesRV.adapter = MyCoursesAdapter(list, context!!)

        myCoursesRV.adapter =
            MyCoursesAdapter(
                list, context!!
            )

    }
}
package com.example.finalproject.ui.passingcourse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.adapters.passingcourse.VideosAdapter
import com.example.finalproject.models.VideoModel
import com.google.firebase.database.*


class VideoContentFragment(val idCourse: String) : Fragment() {

    private lateinit var videosRecyclerView: RecyclerView
    private val CHILD: String = "CourseData"
    private val CHILD_VIDEO = "dataVideoPassCourse"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View =
            inflater.inflate(R.layout.fragement_passing_course_video, container, false)!!
        getDataForPassCourse(idCourse, view)
        return view
    }

    private fun getDataForPassCourse(idCourse: String, view: View) {
        val list: ArrayList<VideoModel> = arrayListOf()
        val reference: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child(CHILD).child(idCourse).child(CHILD_VIDEO)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    val model: VideoModel? =
                        it.getValue(VideoModel::class.java)
                    list.add(
                        VideoModel(
                            model!!.titleVideo,
                            model.idVideoYoutube,
                            model.urlPreview
                        )
                    )
                }
                initRecyclerReviews(list, view)
            }

            override fun onCancelled(p0: DatabaseError) {
                //not implemented
            }
        })
    }


    private fun initRecyclerReviews(list: ArrayList<VideoModel>, view: View) {
        videosRecyclerView = view.findViewById(R.id.videosRecycler)
        videosRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        videosRecyclerView.adapter =
            VideosAdapter(list, context!!)
    }


}
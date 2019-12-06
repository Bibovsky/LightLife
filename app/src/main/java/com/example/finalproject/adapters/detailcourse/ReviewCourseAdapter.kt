package com.example.finalproject.adapters.detailcourse


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.models.ReviewModel


class ReviewCourseAdapter(private val list: ArrayList<ReviewModel>, val context: Context) :
    RecyclerView.Adapter<ReviewCourseAdapter.ItemPostHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemPostHolder {
        return ItemPostHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.review_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ItemPostHolder, position: Int) {
        val post = list[position]
        holder.bind(post)
    }

    class ItemPostHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameReviewer = itemView.findViewById<TextView>(R.id.name_reviewer)
        private val textReview = itemView.findViewById<TextView>(R.id.text_review)
        //private val imgReviewer =
        fun bind(post: ReviewModel) {
            nameReviewer.text = post.nameReviewer
            textReview.text = post.textContent
        }
    }


}




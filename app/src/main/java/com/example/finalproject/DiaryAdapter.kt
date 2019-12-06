package com.example.finalproject

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.models.DiaryModel

class DiaryAdapter(val diaryList: MutableList<DiaryModel>) :
    RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder>() {
    lateinit var context: Context

    inner class DiaryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var timeText = view.findViewById<TextView>(R.id.timeTextView)
        var actionText = view.findViewById<TextView>(R.id.actionTextView)
        fun bind(position: Int) {
            actionText.text = diaryList[position].action
            timeText.text = diaryList[position].date

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.diary_item, parent, false)
        context = parent.context
        return DiaryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return diaryList.size
    }


    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        holder.bind(position)
    }


}
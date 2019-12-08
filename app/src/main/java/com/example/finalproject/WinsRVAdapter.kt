package com.example.finalproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.models.WinsList

class WinsRVAdapter(val winsList: ArrayList<WinsList>) :
    RecyclerView.Adapter<WinsRVAdapter.WinsViewHolder>() {
    lateinit var context: Context

    inner class WinsViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val winName: TextView = itemview.findViewById(R.id.win_name)
        val winDesc: TextView = itemview.findViewById(R.id.win_desc)
        val winstartTime: TextView = itemview.findViewById(R.id.start_win_time)
        val winFinishTime: TextView = itemview.findViewById(R.id.finish_win_time)

        fun bind(position: Int) {
            winName.text = winsList[position].name
            winDesc.text = winsList[position].desc
            winstartTime.text = winsList[position].startTime
            winFinishTime.text = "Завершено: " + winsList[position].finishTime
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WinsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.wins_item, parent, false)
        context = parent.context
        return WinsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return winsList.size
    }

    override fun onBindViewHolder(holder: WinsViewHolder, position: Int) {
        holder.bind(position)
    }
}
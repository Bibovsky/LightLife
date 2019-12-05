package com.example.finalproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WinsRVAdapter(val winsList: ArrayList<String>) :
    RecyclerView.Adapter<WinsRVAdapter.WinsViewHolder>() {
    lateinit var context:Context
    inner class WinsViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val winName: TextView = itemview.findViewById(R.id.win_name)
        val winDesc: TextView = itemview.findViewById(R.id.win_desc)

        fun bind(position: Int) {

            winName.text = winsList[position]
            winDesc.text = winsList[position]
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WinsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.wins_item, parent, false)
        context=parent.context
        return WinsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return winsList.size
    }

    override fun onBindViewHolder(holder: WinsViewHolder, position: Int) {
        holder.bind(position)
    }
}
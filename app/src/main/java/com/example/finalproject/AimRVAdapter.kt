package com.example.finalproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AimRVAdapter(val aimsList: ArrayList<String>) :
    RecyclerView.Adapter<AimRVAdapter.AimViewHolder>() {
    inner class AimViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val aimName: TextView = itemView.findViewById(R.id.aim_name)
        val aimDesc: TextView = itemview.findViewById(R.id.aim_desc)
        fun bind(position: Int){
            aimName.text=aimsList[position]
            aimDesc.text=aimsList[position]
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AimViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.aim_item, parent, false)
        return AimViewHolder(view)
    }

    override fun getItemCount(): Int {
        return aimsList.size
    }

    override fun onBindViewHolder(holder: AimViewHolder, position: Int) {
        holder.bind(position)
    }
}
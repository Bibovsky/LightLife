package com.example.finalproject

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.models.Constant
import com.example.finalproject.ui.user.User


class AimRVAdapter(val aimsList: ArrayList<String>) :
    RecyclerView.Adapter<AimRVAdapter.AimViewHolder>() {
    lateinit var context: Context

    inner class AimViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        val aimName: TextView = itemView.findViewById(R.id.aim_name)
        val aimDesc: TextView = itemview.findViewById(R.id.aim_desc)
        val chronometer = itemView.findViewById<Chronometer>(R.id.chronometer)
        val button: Button = itemView.findViewById(R.id.aim_btn)

        var isPlaying = false
        val user = Constant.context
        fun bind(position: Int) {
            val winsList = user.getWinsList()
            val winsRV = user.getRV()
            chronometer.text = "00:00:00"
            chronometer.setOnChronometerTickListener {
                val time =
                    SystemClock.elapsedRealtime() - chronometer.base
                val h = (time / 3600000).toInt()
                val m = (time - h * 3600000).toInt() / 60000
                val s = (time - h * 3600000 - m * 60000).toInt() / 1000
                val t =
                    (if (h < 10) "0$h" else h).toString() + ":" + (if (m < 10) "0$m" else m) + ":" + if (s < 10) "0$s" else s
                chronometer.text = t
                Constant.chronotext=chronometer.text.toString()
            }
            button.setOnClickListener() {

                if (!isPlaying) {
                    chronometer.base = SystemClock.elapsedRealtime()
                    chronometer.start()
                    Log.e("Start", "started")
                    isPlaying = true
                } else if (isPlaying) {
                    chronometer.stop()
                    Log.e("Stop", "Stopped")
                    isPlaying = false

                    aimsList.removeAt(position)
                    winsList.add(chronometer.text.toString())
                    winsRV.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true)
                    winsRV.scrollToPosition(winsList.size - 1)
                    winsRV.adapter = WinsRVAdapter(winsList)

                    notifyDataSetChanged()
                }
                notifyDataSetChanged()
                button.text = if (isPlaying!!) "Закончить" else "Начать"

            }
            aimName.text = aimsList[position]
            aimDesc.text = aimsList[position]
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AimViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.aim_item, parent, false)
        context = parent.context
        return AimViewHolder(view)
    }

    override fun getItemCount(): Int {
        return aimsList.size
    }

    override fun onBindViewHolder(holder: AimViewHolder, position: Int) {
        holder.bind(position)
    }
}
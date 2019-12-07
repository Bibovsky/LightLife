package com.example.finalproject

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.models.AimsList
import com.example.finalproject.models.Constant
import com.example.finalproject.models.WinsList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*


class AimRVAdapter(val aimsList: ArrayList<AimsList>) :
    RecyclerView.Adapter<AimRVAdapter.AimViewHolder>() {
    lateinit var context: Context
    private lateinit var mDatebase: FirebaseDatabase
    private lateinit var mReference: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mUser: FirebaseUser
    lateinit var winsReference: DatabaseReference
    lateinit var aimsReference: DatabaseReference
    var isPlaying = false
        //    lateinit var aimSp:SharedPreferences
   // lateinit var aimSPEditor: SharedPreferences.Editor

    inner class AimViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        val aimName: TextView = itemView.findViewById(R.id.aim_name)
        val aimDesc: TextView = itemview.findViewById(R.id.aim_desc)
        val startTimeTV: TextView = itemview.findViewById(R.id.start_aim_time)
        val button: Button = itemView.findViewById(R.id.aim_btn)
        val user = Constant.context

        fun bind(position: Int) {

            mDatebase = FirebaseDatabase.getInstance()
            mAuth = FirebaseAuth.getInstance()
            mUser = mAuth.currentUser!!
            mReference = mDatebase.reference.child("Users").child(mUser.uid)
            aimsReference = mReference.child("aims")
            winsReference = mReference.child("wins")
            val winsList = user.getWinsList()
            val aimsList = user.getAimsList()

            var aimStartTime: String=""
            val aimsRV = user.getAimsRv()
            val winsRV = user.getWinsRv()

            winsReference.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Log.e("Firebase", "ERROR")
                }
                override fun onDataChange(p0: DataSnapshot) {
                    var wins = arrayListOf<WinsList>()
                    for (child in p0.children) {
                        var winName = child.child("name").value.toString()
                        var winDesc = child.child("desc").value.toString()
                        var winStartTime=child.child("startTime").value.toString()
                        var winFinishTime = child.child("finishTime").value.toString()

                        wins.add(WinsList(winName, winDesc,winStartTime, winFinishTime))
                    }
                    val lm = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    winsRV.layoutManager = lm
                    winsRV.adapter = WinsRVAdapter(wins)
                    lm.scrollToPosition(WinsRVAdapter(wins).itemCount - 1)
                }


            })
          /*  aimSp= (context as BottomNavActivity).getPreferences(MODE_PRIVATE)

            if (aimSp.getString("start_time","")!=""){
                startTimeTV.text=aimSp.getString("start_time","")

            }*/
            button.setOnClickListener() {
                val mRef = mDatebase.reference.child("Users").child(mUser.uid).child("aims")

                if (!isPlaying) {
                    //mRef.child("-LvXGXdlHcLzjwmUbjwA").child("name").setValue("fghjk")
                  //  (context as BottomNavActivity).startService(Intent(context,AimService::class.java))
                    Log.e("Start", "started")
                    startTimeTV.text =
                        "Начало выполнения: " + SimpleDateFormat("hh:mm:ss").format(Date())
                    aimStartTime = SimpleDateFormat("hh:mm:ss").format(Date())
                    isPlaying = true
                    val query = mDatebase.reference.child("Users").child(mUser.uid).child("aims")
                        .orderByChild("name").equalTo(aimsList[position].name)

                    query.addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            Log.e("Firebase", "ERROR")
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            for (child in p0.children) {
                                mDatebase.reference.child("Users").child(mUser.uid).child("aims")
                                    .child(child.key.toString()).child("playing").setValue(true)
                                    .addOnSuccessListener {
                                        Log.e("name", "SUCCESS")
                                    }
                            }
                        }
                    })/*
                    aimSPEditor=aimSp.edit()
                    aimSPEditor.putString("start_time",aimStartTime)
                    aimSPEditor.apply()*/
                   // Log.e("sp",aimSp!!.getString("start_time",""))
                } else if (isPlaying) {
                    //(context as BottomNavActivity).stopService(Intent(context as BottomNavActivity,AimService::class.java))

                    Log.e("Stop", "Stopped")
                    isPlaying = false
                    aimsRV.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    aimsRV.adapter = AimRVAdapter(aimsList)
                    winsList.add(
                        WinsList(
                            aimsList[position].name,
                            aimsList[position].desc,
                            aimStartTime,
                            SimpleDateFormat("hh:mm:ss").format(Date())
                        )
                    )

                    winsReference.push().setValue(
                        WinsList(
                            aimsList[position].name,
                            aimsList[position].desc,
                            aimStartTime,
                            SimpleDateFormat("hh:mm:ss").format(Date())

                        )
                    )

                    val itemName = aimsList[position].name

                    val query = mDatebase.reference.child("Users").child(mUser.uid).child("aims")
                        .orderByChild("name").equalTo(itemName)

                    query.addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            Log.e("Firebase", "ERROR")
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            for (child in p0.children) {
                                mDatebase.reference.child("Users").child(mUser.uid).child("aims")
                                    .child(child.key.toString()).setValue(null)
                                    .addOnSuccessListener {
                                        Log.e("name", "SUCCESS")
                                    }
                            }
                        }
                    })
                    aimsList.removeAt(position)


                }
                button.text = if (isPlaying!!) "Закончить" else "Начать"

            }
            aimName.text = aimsList[position].name
            aimDesc.text = aimsList[position].desc
            //startTimeTV.text=aimsList[position].startTime
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
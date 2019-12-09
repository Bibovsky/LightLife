package com.example.finalproject.adapters.useradapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
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
            val aimsRV = user.getAimsRv()
            val winsRV = user.getWinsRv()
            var aimStartTime: String = "00:00:00"
            val getIsPlayingData =
                mDatebase.reference.child("Users").child(mUser.uid).child("aims")

            getIsPlayingData.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Log.e("Firebase", "ERROR")
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val timeList = arrayListOf<String>()
                    for (child in p0.children) {

                        Log.e("true", true.toString())
                        timeList.add(child.child("startAimTime").value.toString())


                        // aimStartTime = SimpleDateFormat("hh:mm:ss").format(Date())
                    }
                    try {
                        startTimeTV.text = "Начало: " +
                                timeList[position]
                        aimStartTime = startTimeTV.text.toString()


                    } catch (err: IndexOutOfBoundsException) {
                    }

                }


            })
            winsReference.addValueEventListener(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        Log.e("Firebase", "ERROR")
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        var wins = arrayListOf<WinsList>()
                        for (child in p0.children) {
                            var winName = child.child("name").value.toString()
                            var winDesc = child.child("desc").value.toString()
                            var winStartTime = child.child("startTime").value.toString()
                            var winFinishTime = child.child("finishTime").value.toString()

                            wins.add(WinsList(winName, winDesc, winStartTime, winFinishTime))
                        }
                        val lm = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        winsRV.layoutManager = lm
                        winsRV.adapter =
                            WinsRVAdapter(
                                wins
                            )
                        lm.scrollToPosition(
                            WinsRVAdapter(
                                wins
                            ).itemCount - 1)
                    }


                })

            button.setOnClickListener()
            {

                if (!aimsList[position].isPlaying) {
                    // Log.e("Start", "started")
                    aimsList[position].isPlaying = true

                    // Constant.currentAims++
                    val changeToTrue =
                        mDatebase.reference.child("Users").child(mUser.uid).child("aims")
                            .orderByChild("name").equalTo(aimsList[position].name)

                    changeToTrue.addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            Log.e("Firebase", "ERROR")
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            for (child in p0.children) {
                                Log.e(
                                    "test",
                                    p0.child(child.key.toString()).child("playing").value.toString()
                                )
                                if (p0.child(child.key.toString()).child("playing").value.toString() == "false") {
                                    mDatebase.reference.child("Users").child(mUser.uid)
                                        .child("aims")
                                        .child(child.key.toString()).child("playing").setValue(true)
                                        .addOnSuccessListener {
                                            Log.e("name", "SUCCESS")

                                        }

                                    mDatebase.reference.child("Users").child(mUser.uid)
                                        .child("aims")
                                        .child(child.key.toString()).child("startAimTime")
                                        .setValue(SimpleDateFormat("hh:mm:ss").format(Date()))
                                        .addOnSuccessListener {
                                            Log.e("name", "SUCCESS time")
                                        }
                                }
                            }
                        }

                    })

                } else if (aimsList[position].isPlaying) {

                    Log.e("Stop", "Stopped")
                    isPlaying = false

                    aimsRV.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    aimsRV.adapter =
                        AimRVAdapter(
                            aimsList
                        )
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
                // button.text = if (aimsList[position].isPlaying!!) "Закончить" else "Начать"

            }
            aimName.text = aimsList[position].name
            aimDesc.text = aimsList[position].desc
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
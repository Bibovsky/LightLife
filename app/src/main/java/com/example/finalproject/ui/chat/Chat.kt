package com.example.finalproject.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.adapters.chat.DialogsAdapter
import com.example.finalproject.models.MessageModel
import java.util.*
import kotlin.collections.ArrayList

class Chat : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_chat, container, false)

        val recycler = root.findViewById<RecyclerView>(R.id.dialog_recycler)
        recycler.layoutManager = LinearLayoutManager(context)
        var dialogsList = getData()
        recycler.adapter = DialogsAdapter(dialogsList, context!!)

        return root
    }

    private fun getData(): List<MessageModel> {
        var list: ArrayList<MessageModel> = arrayListOf()
        for (i in 1..5) {
            list.add(MessageModel(1, "Сообщение", Date(), true))
        }
        return list
    }
}



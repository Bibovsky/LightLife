package com.example.finalproject.ui.chat

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.adapters.chat.MessageRecyclerAdapter
import com.example.finalproject.models.MessageModel
import java.util.*

class ChatActivity : AppCompatActivity()  {
    private lateinit var messageField: EditText
    private lateinit var sendMessage: ImageButton
    private lateinit var messageRecycler: RecyclerView
    private lateinit var messageList: MutableList<MessageModel>
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        init()
        setListeners()

        val dataMessages: MutableList<MessageModel> = receiveMessagesFromServer()
        setDataInRecyclerView(dataMessages)
        scrollBottom()
    }

    private fun init() {
        messageList = mutableListOf()
        layoutManager = LinearLayoutManager(applicationContext)

        sendMessage = findViewById(R.id.send_message_button)
        messageField = findViewById(R.id.message_et)
        messageRecycler = findViewById(R.id.message_recycler_view)
    }

    private fun setListeners() {
        sendMessage.setOnClickListener {
            if (messageField.text.toString().trim() != "") {
                addMessage(messageField.text.toString().trim(), Date(), true)
                messageField.setText("")
                scrollBottom()
            }
        }
    }

    private fun setDataInRecyclerView(dataMessages: MutableList<MessageModel>) {
        messageList.addAll(dataMessages)
        messageRecycler.layoutManager = layoutManager
        messageRecycler.adapter = MessageRecyclerAdapter(messageList)
    }

    private fun addMessage(message: String, date: Date, isUser: Boolean) {
        messageList.add(MessageModel(1, message, date, isUser))
    }

    private fun receiveMessagesFromServer(): MutableList<MessageModel> {
        //FOR EXAMPLE
        val dataMessages: MutableList<MessageModel> = mutableListOf()
        for (i in 1..20) {
            //разделим для наглядности
            if (i % 2 == 0) {
                dataMessages.add(MessageModel(1, "This is my text!", Date(), true))
            } else {
                dataMessages.add(MessageModel(1, "The text of other user", Date(), false))
            }
        }
        return dataMessages
    }


    private fun scrollBottom() {
        messageRecycler.scrollToPosition(messageList.count() - 1)
    }


}

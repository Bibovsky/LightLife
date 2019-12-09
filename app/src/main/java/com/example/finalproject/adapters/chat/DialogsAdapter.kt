package com.example.finalproject.adapters.chat


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.models.MessageModel
import com.example.finalproject.ui.chat.ChatActivity


class DialogsAdapter(private val list: List<MessageModel>, val context: Context) :
    RecyclerView.Adapter<DialogsAdapter.DialogsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogsHolder {
        return DialogsHolder(
            LayoutInflater.from(context).inflate(
                R.layout.dialogs_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: DialogsHolder, position: Int) {
        val photo = list[position]
        holder.bind(photo)
    }

    inner class DialogsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView = itemView.findViewById<ImageView>(R.id.photos_image_view)
        private val idTextView = itemView.findViewById<TextView>(R.id.id_text)
        fun bind(post: MessageModel) {
            idTextView.text = "UserName"
            //typeTextView.text =

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, ChatActivity::class.java)
                //go to new activity
                itemView.context.startActivity(intent)
            }
        }
    }
}
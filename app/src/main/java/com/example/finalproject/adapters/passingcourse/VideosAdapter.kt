package com.example.finalproject.adapters.passingcourse


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.finalproject.R
import com.example.finalproject.models.VideoModel
import com.example.finalproject.ui.passingcourse.VideoPlayActivity
import jp.wasabeef.glide.transformations.BlurTransformation


class VideosAdapter(private val list: ArrayList<VideoModel>, val context: Context) :
    RecyclerView.Adapter<VideosAdapter.ItemPostHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemPostHolder {
        return ItemPostHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.video_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ItemPostHolder, position: Int) {
        val post = list[position]
        holder.bind(post)

        holder.itemView.setOnClickListener {
            startVideo(post.idVideoYoutube)
        }
    }

    inner class ItemPostHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleVideo = itemView.findViewById<TextView>(R.id.titleVideo)
        private val previewImageView = itemView.findViewById<ImageView>(R.id.previewVideo)
        fun bind(post: VideoModel) {
            titleVideo.text = post.titleVideo

            Glide.with(context)
                .load(post.urlPreview)
                .apply(RequestOptions.bitmapTransform(BlurTransformation(5, 3)))
                .into(previewImageView)
        }
    }


    private fun startVideo(idVideoYoutube: String){
        val intent = Intent(context, VideoPlayActivity::class.java)
        intent.putExtra("ID_VIDEO_YOUTUBE", idVideoYoutube)
        ContextCompat.startActivity(context, intent, bundleOf())
    }


}




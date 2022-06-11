package com.github.yeeun_yun97.toy.linksaver.ui.adapter.recycler

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.github.yeeun_yun97.clone.ynmodule.ui.adapter.RecyclerBasicAdapter
import com.github.yeeun_yun97.clone.ynmodule.ui.adapter.RecyclerBasicViewHolder
import com.github.yeeun_yun97.toy.linksaver.data.model.VideoData
import com.github.yeeun_yun97.toy.linksaver.databinding.ItemVideoListDetailBinding
import com.google.android.exoplayer2.ExoPlayer

class VideoListAdapter(
    private val player: ExoPlayer,
    private val detailOperation: (Int) -> Unit
) :
    RecyclerBasicAdapter<VideoData, VideoViewHolder>() {
    override fun onBindViewHolder(
        holder: VideoViewHolder,
        item: VideoData
    ) {
    }

    override fun onBindViewHolder(
        holder: VideoViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        holder.setData(position, itemList[position], detailOperation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding =
            ItemVideoListDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder(player, binding)
    }
}

class VideoViewHolder(private val player: ExoPlayer, binding: ItemVideoListDetailBinding) :
    RecyclerBasicViewHolder<ItemVideoListDetailBinding>(binding) {

    private var index: Int = -1
    private lateinit var previewUrl: String
    private lateinit var videoUrl: String
    private var isYoutubeVideo: Boolean = false

    fun setData(index: Int, data: VideoData, detailOperation: (Int) -> Unit) {
        this.index = index
        binding.data = data
        binding.root.setOnClickListener { detailOperation(data.lid) }

        // set video data
        previewUrl = data.thumbnail
        videoUrl = data.url
        isYoutubeVideo = data.isYoutubeVideo

        // hide playerView
        binding.playerView.visibility = View.INVISIBLE

        // show thumbnail
        binding.thumbnailImageView.visibility = View.VISIBLE
        binding.thumbnailImageView.setBackgroundColor(Color.BLACK)
        setThumbnailImage(itemView.context, binding.thumbnailImageView)

    }

    fun playStop() {
        player.pause()
        binding.playerView.player = null
        binding.playerView.visibility = View.INVISIBLE
        binding.thumbnailImageView.visibility = View.VISIBLE
    }

    fun playStart() {
        binding.playerView.player = player

        player.playWhenReady = true
        player.seekToDefaultPosition(index)

        binding.playerView.visibility = View.VISIBLE
        binding.thumbnailImageView.visibility = View.GONE
    }


    private fun setThumbnailImage(context: Context, imageView: ImageView) {
        if (previewUrl.isNotEmpty())
            Glide.with(context).load(previewUrl).fitCenter()
                .into(imageView)
        else {
            Glide.with(context)
                .load(videoUrl)
                .centerCrop()
                .override(720, 360)
                .into(imageView)
        }
    }


}
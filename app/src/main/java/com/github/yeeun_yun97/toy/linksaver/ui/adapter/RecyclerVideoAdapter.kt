package com.github.yeeun_yun97.toy.linksaver.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.github.yeeun_yun97.clone.ynmodule.ui.adapter.RecyclerBasicAdapter
import com.github.yeeun_yun97.clone.ynmodule.ui.adapter.RecyclerBasicViewHolder
import com.github.yeeun_yun97.toy.linksaver.databinding.ItemVideoListDetailBinding
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.ListVideoFragment
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

class RecyclerVideoAdapter(val player: ExoPlayer, val detailOperation: (Int) -> Unit) :
    RecyclerBasicAdapter<ListVideoFragment.VideoData, VideoRecyclerViewHolder>() {
    override fun onBindViewHolder(
        holder: VideoRecyclerViewHolder,
        item: ListVideoFragment.VideoData
    ) {
        holder.setData(item, detailOperation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoRecyclerViewHolder {
        val binding =
            ItemVideoListDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoRecyclerViewHolder(player, binding)
    }
}

class VideoRecyclerViewHolder(val player: ExoPlayer, binding: ItemVideoListDetailBinding) :
    RecyclerBasicViewHolder<ItemVideoListDetailBinding>(binding) {
    lateinit var previewUrl: String
    lateinit var videoUrl: String

    fun playStart() {
        binding.playerView.player = player
//        binding.gradientImageView.visibility = View.GONE
        binding.playerView.visibility = View.VISIBLE
        binding.thumbnailImageView.visibility = View.GONE

        val mediaItem: MediaItem = MediaItem.Builder()
            .setUri(videoUrl)
            .setClippingConfiguration(
                MediaItem.ClippingConfiguration.Builder()
                    .setStartPositionMs(1000)
                    .setEndPositionMs(20000)
                    .build()
            ).build()
        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = true
    }

    fun playStop() {
        binding.playerView.player = null
        player.clearMediaItems()
        binding.playerView.visibility = View.INVISIBLE
        binding.thumbnailImageView.visibility = View.VISIBLE
    }

    @SuppressLint("CheckResult")
    fun setData(data: ListVideoFragment.VideoData, detailOperation: (Int) -> Unit) {
        binding.data = data
        binding.root.setOnClickListener { detailOperation(data.lid) }
//        binding.gradientImageView.visibility = View.INVISIBLE

        previewUrl = data.thumbnail
        videoUrl = data.url

        binding.playerView.visibility = View.INVISIBLE
        binding.thumbnailImageView.visibility = View.VISIBLE

        binding.thumbnailImageView.setBackgroundColor(Color.BLACK)
        if (previewUrl.isNotEmpty())
            Glide.with(itemView.context).load(previewUrl).fitCenter()
                .into(binding.thumbnailImageView)
        else {
            Glide.with(itemView.context)
                .load(videoUrl)
                .centerCrop()
                .override(720, 360)
                .into(binding.thumbnailImageView)
        }

//            Glide.with(itemView.context)
//                .asBitmap()
//                .load(previewUrl)
//                .listener(object : RequestListener<Bitmap?> {
//                    override fun onLoadFailed(
//                        e: GlideException?,
//                        model: Any?,
//                        target: Target<Bitmap?>?,
//                        isFirstResource: Boolean
//                    ): Boolean {
//                        Log.e("thumb fail", "fail")
//                        return false
//                    }
//
//                    override fun onResourceReady(
//                        resource: Bitmap?,
//                        model: Any?,
//                        target: Target<Bitmap?>?,
//                        dataSource: DataSource?,
//                        isFirstResource: Boolean
//                    ): Boolean {
//                        if (resource != null) {
//                            binding.playerView.setThumbnailImage(resource)
//                            Log.e("thumb success", "success")
//                        } else {
//                            Log.e("thumb success", "null")
//                        }
//                        return false
//                    }
//                })
//                .preload()

//            var File = File(previewUrl)
//           val thumb = ThumbnailUtils.extractThumbnail()
//            binding.playerView.defaultArtwork=BitmapDrawable(thumb)
    }


}
package com.github.yeeun_yun97.toy.linksaver.ui.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.yeeun_yun97.clone.ynmodule.ui.adapter.RecyclerBasicAdapter
import com.github.yeeun_yun97.clone.ynmodule.ui.adapter.RecyclerBasicViewHolder
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentListVideoBinding
import com.github.yeeun_yun97.toy.linksaver.databinding.ItemVideoListDetailBinding
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player

class ListVideoFragment : SjBasicFragment<FragmentListVideoBinding>() {

    private var _player: ExoPlayer? = null
    private val player: ExoPlayer get() = _player!!

    data class VideoData(
        val url: String,
        val name: String,
        val thumbnail: String,
        val tagList: List<SjTag>
    )

    class VideoRecyclerAdapter(val player: ExoPlayer) :
        RecyclerBasicAdapter<VideoData, VideoRecyclerViewHolder>() {
        override fun onBindViewHolder(
            holder: VideoRecyclerViewHolder,
            item: VideoData
        ) {
            holder.setData(item)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoRecyclerViewHolder {
            val binding = ItemVideoListDetailBinding.inflate(LayoutInflater.from(parent.context))
            return VideoRecyclerViewHolder(player, binding)
        }
    }

    class VideoRecyclerViewHolder(val player: ExoPlayer, binding: ItemVideoListDetailBinding) :
        RecyclerBasicViewHolder<ItemVideoListDetailBinding>(binding) {
        lateinit var previewUrl: String
        lateinit var videoUrl: String


        fun playStart() {
            binding.playerView.player=player
            binding.gradientImageView.visibility=View.GONE
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
            player.playWhenReady=true
        }

        fun playStop() {
            binding.playerView.player=null
            player.clearMediaItems()
            binding.playerView.visibility = View.INVISIBLE
            binding.thumbnailImageView.visibility = View.VISIBLE
        }

        @SuppressLint("CheckResult")
        fun setData(data: VideoData) {
            binding.data = data
            binding.gradientImageView.visibility = View.INVISIBLE

            previewUrl = data.thumbnail
            videoUrl = data.url

            binding.playerView.visibility = View.INVISIBLE
            binding.thumbnailImageView.visibility = View.VISIBLE

            binding.thumbnailImageView.setBackgroundColor(Color.BLACK)
            Glide.with(itemView.context).load(previewUrl).fitCenter()
                .into(binding.thumbnailImageView)

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


    override fun layoutId(): Int = R.layout.fragment_list_video

    override fun onCreateView() {

        _player = ExoPlayer.Builder(requireContext()).build()
        player.repeatMode = Player.REPEAT_MODE_ONE

        val manager = LinearLayoutManager(context)
        binding.videoRecyclerView.layoutManager = manager
        val adapter = VideoRecyclerAdapter(player)
        binding.videoRecyclerView.adapter = adapter
        val list = listOf(
            VideoData(
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                "Big Buck Bunny",

                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/BigBuckBunny.jpg",
                listOf(
                    SjTag(name = "Bunny"),
                    SjTag(name = "Big"),
                    SjTag(name = "Blender")
                )
            ),

            VideoData(
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
                "For Bigger Blazes",
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerBlazes.jpg",
                listOf(
                    SjTag(name = "Blaze"),
                    SjTag(name = "google")
                )
            ),

            VideoData(
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
                "Elephant Dream",
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ElephantsDream.jpg",
                listOf(
                    SjTag(name = "Elephant"),
                    SjTag(name = "Blender")
                )
            ),

            VideoData(
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WhatCarCanYouGetForAGrand.mp4",
                "What care can you get for a grand?",
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/WhatCarCanYouGetForAGrand.jpg",
                listOf(
                    SjTag(name = "Blend"),
                    SjTag(name = "Garage419")
                )
            ),


            VideoData(
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
                "For Bigger Escape",
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerEscapes.jpg",
                listOf(
                    SjTag(name = "Escape"),
                    SjTag(name = "Google")
                )
            )
        )
        adapter.setList(list)

        binding.videoRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            @SuppressLint("StaticFieldLeak")
            private var prevViewHolder: VideoRecyclerViewHolder? = null

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val index = manager.findFirstCompletelyVisibleItemPosition()
                val viewHolder =
                    binding.videoRecyclerView.findViewHolderForLayoutPosition(index) as VideoRecyclerViewHolder
                    prevViewHolder?.playStop()
                prevViewHolder =viewHolder
                viewHolder.playStart()
            }
        })


    }

    override fun onStop() {
        super.onStop()

        // release player
        player.release()
        _player = null
    }


}
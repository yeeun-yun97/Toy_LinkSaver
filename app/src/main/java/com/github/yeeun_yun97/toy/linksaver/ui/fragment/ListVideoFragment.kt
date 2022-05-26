package com.github.yeeun_yun97.toy.linksaver.ui.fragment

import android.annotation.SuppressLint
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentListVideoBinding
import com.github.yeeun_yun97.toy.linksaver.ui.adapter.RecyclerVideoAdapter
import com.github.yeeun_yun97.toy.linksaver.ui.adapter.VideoRecyclerViewHolder
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import com.google.android.exoplayer2.ExoPlayer
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

    override fun layoutId(): Int = R.layout.fragment_list_video

    override fun onCreateView() {

        _player = ExoPlayer.Builder(requireContext()).build()
        player.repeatMode = Player.REPEAT_MODE_ONE

        val manager = LinearLayoutManager(context)
        binding.videoRecyclerView.layoutManager = manager
        val adapter = RecyclerVideoAdapter(player)
        binding.videoRecyclerView.adapter = adapter

        adapter.setList(getDataList())

        binding.videoRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            @SuppressLint("StaticFieldLeak")
            private var prevViewHolder: VideoRecyclerViewHolder? = null

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val index = manager.findFirstCompletelyVisibleItemPosition()
                val currentViewHolder =
                    binding.videoRecyclerView.findViewHolderForLayoutPosition(index) as VideoRecyclerViewHolder
                prevViewHolder?.playStop()
                currentViewHolder.playStart()
                prevViewHolder = currentViewHolder
            }
        })

    }

    override fun onStop() {
        super.onStop()

        // release player
        player.release()
        _player = null
    }

    private fun getDataList(): List<VideoData> {
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
        return list
    }




}
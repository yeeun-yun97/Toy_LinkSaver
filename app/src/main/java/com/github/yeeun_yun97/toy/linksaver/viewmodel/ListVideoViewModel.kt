package com.github.yeeun_yun97.toy.linksaver.viewmodel

import android.content.Context
import android.util.Log
import android.util.SparseArray
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.yeeun_yun97.toy.linksaver.data.model.LinkModelUtil
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.data.model.VideoData
import com.github.yeeun_yun97.toy.linksaver.ui.component.SjUtil
import com.github.yeeun_yun97.toy.linksaver.viewmodel.basic.BasicViewModelWithRepository
import com.google.android.exoplayer2.MediaItem

class ListVideoViewModel : BasicViewModelWithRepository() {

    private val allVideoList = repository.linkTypeVideoList
    val allVideoData: LiveData<MutableList<VideoData>> get() = _allVideoData
    private var _allVideoData = MutableLiveData(mutableListOf<VideoData>())

    var playList = MutableLiveData(mutableListOf<MediaItem>())

    init {
        allVideoList.observeForever {
            val videos = mutableListOf<VideoData>()
            for (i in it.indices) {
                val vid = it[i]
                Log.d("비디오 불러옴", vid.toString())
                val fullUrl = LinkModelUtil.getFullUrl(vid)
                val videoData = VideoData(
                    vid.link.lid, fullUrl, vid.link.name, vid.link.preview,
                    SjUtil.checkYoutubePrefix(fullUrl), vid.tags
                )
                videos.add(videoData)
            }
            _allVideoData.postValue(videos)
        }

    }


    fun getDataList(): List<VideoData> {
        val list = listOf(
            VideoData(
                url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                name = "Big Buck Bunny",

                thumbnail = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/BigBuckBunny.jpg",
                tagList = listOf(
                    SjTag(name = "Bunny"),
                    SjTag(name = "Big"),
                    SjTag(name = "Blender")
                ),
                isYoutubeVideo = false
            )
//            ,
//
//            ListVideoFragment.VideoData(
//                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
//                "For Bigger Blazes",
//                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerBlazes.jpg",
//                listOf(
//                    SjTag(name = "Blaze"),
//                    SjTag(name = "google")
//                )
//            ),
//
//            ListVideoFragment.VideoData(
//                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
//                "Elephant Dream",
//                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ElephantsDream.jpg",
//                listOf(
//                    SjTag(name = "Elephant"),
//                    SjTag(name = "Blender")
//                )
//            ),
//
//            ListVideoFragment.VideoData(
//                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WhatCarCanYouGetForAGrand.mp4",
//                "What care can you get for a grand?",
//                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/WhatCarCanYouGetForAGrand.jpg",
//                listOf(
//                    SjTag(name = "Blend"),
//                    SjTag(name = "Garage419")
//                )
//            ),
//
//
//            ListVideoFragment.VideoData(
//                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
//                "For Bigger Escape",
//                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerEscapes.jpg",
//                listOf(
//                    SjTag(name = "Escape"),
//                    SjTag(name = "Google")
//                )
//            )
        )
        return list
    }
}
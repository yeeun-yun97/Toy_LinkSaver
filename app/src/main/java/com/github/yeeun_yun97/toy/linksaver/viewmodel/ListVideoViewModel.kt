package com.github.yeeun_yun97.toy.linksaver.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.yeeun_yun97.toy.linksaver.data.model.LinkModelUtil
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
}
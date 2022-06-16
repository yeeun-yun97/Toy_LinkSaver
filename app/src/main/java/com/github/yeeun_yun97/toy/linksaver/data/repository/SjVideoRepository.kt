package com.github.yeeun_yun97.toy.linksaver.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.yeeun_yun97.toy.linksaver.data.dao.SjDao
import com.github.yeeun_yun97.toy.linksaver.data.db.SjDatabaseUtil
import com.github.yeeun_yun97.toy.linksaver.data.model.ELinkType
import com.github.yeeun_yun97.toy.linksaver.data.model.LinkModelUtil
import com.github.yeeun_yun97.toy.linksaver.data.model.VideoData
import com.github.yeeun_yun97.toy.linksaver.ui.component.SjUtil

class SjVideoRepository private constructor() {
    // dao
    private val dao: SjDao = SjDatabaseUtil.getDao()

    // for video list fragment
    private val linkTypeVideoList = dao.getAllLinksByType(ELinkType.video.name)
    private val publicLinkTypeVideoList = dao.getPublicLinksByType(ELinkType.video.name)

    private var _allVideoData = MutableLiveData(mutableListOf<VideoData>())
    private var _publicVideoData = MutableLiveData(mutableListOf<VideoData>())
    val allVideoData: LiveData<MutableList<VideoData>> get() = _allVideoData
    val publicVideoData: LiveData<MutableList<VideoData>> get() = _publicVideoData

    init {
        linkTypeVideoList.observeForever {
            val videos = mutableListOf<VideoData>()
            for (i in it.indices) {
                val vid = it[i]
                val fullUrl = LinkModelUtil.getFullUrl(vid)
                val videoData = VideoData(vid.link.lid, fullUrl, vid.link.name, vid.link.preview, SjUtil.checkYoutubePrefix(fullUrl), vid.tags)
                videos.add(videoData)
            }
            _allVideoData.postValue(videos)
        }

        publicLinkTypeVideoList.observeForever{
            val videos = mutableListOf<VideoData>()
            for (i in it.indices) {
                val vid = it[i]
                val fullUrl = LinkModelUtil.getFullUrl(vid)
                val videoData = VideoData(vid.link.lid, fullUrl, vid.link.name, vid.link.preview, SjUtil.checkYoutubePrefix(fullUrl), vid.tags)
                videos.add(videoData)
            }
            _publicVideoData.postValue(videos)
        }
    }

    companion object {
        // singleton object
        private lateinit var repo: SjVideoRepository

        fun getInstance(): SjVideoRepository {
            if (!this::repo.isInitialized) {
                repo = SjVideoRepository()
            }
            return repo
        }

    }


}
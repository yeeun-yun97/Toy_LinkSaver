package com.github.yeeun_yun97.toy.linksaver.data.repository.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.github.yeeun_yun97.toy.linksaver.data.dao.SjLinkDao
import com.github.yeeun_yun97.toy.linksaver.data.db.SjDatabaseUtil
import com.github.yeeun_yun97.toy.linksaver.data.model.ELinkType
import com.github.yeeun_yun97.toy.linksaver.data.model.LinkModelUtil
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLinksAndDomainsWithTags
import com.github.yeeun_yun97.toy.linksaver.data.model.VideoData
import com.github.yeeun_yun97.toy.linksaver.ui.component.SjUtil

class SjVideoRepository private constructor() {
    // dao
    private val dao: SjLinkDao = SjDatabaseUtil.getLinkDao()

    // for video list fragment
    private val _linksVideo = dao.getAllLinksByType(ELinkType.video.name)
    private val _linksVideoPublic = dao.getPublicLinksByType(ELinkType.video.name)

    val linksVideo: LiveData<List<VideoData>> =
        Transformations.switchMap(_linksVideo) { convertToVideoData(it) }
    val linksVideoPublic: LiveData<List<VideoData>> =
        Transformations.switchMap(_linksVideoPublic) { convertToVideoData(it) }

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

    private fun convertToVideoData(dataList: List<SjLinksAndDomainsWithTags>): MutableLiveData<List<VideoData>> {
        val list = mutableListOf<VideoData>()
        for (i in dataList.indices) {
            val vid = dataList[i]
            val fullUrl = LinkModelUtil.getFullUrl(vid)
            val videoData = VideoData(
                vid.link.lid,
                fullUrl,
                vid.link.name,
                vid.link.preview,
                SjUtil.checkYoutubePrefix(fullUrl),
                vid.tags
            )
            list.add(videoData)
        }
        return MutableLiveData(list)
    }

}
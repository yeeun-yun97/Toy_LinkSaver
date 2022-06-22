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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SjVideoRepository private constructor() {
    private val dao: SjLinkDao = SjDatabaseUtil.getLinkDao()

    private val videoType = ELinkType.video.name

    // for video list fragment
    private val _linksVideo = MutableLiveData<List<SjLinksAndDomainsWithTags>>()
    val linksVideo: LiveData<List<VideoData>> = Transformations.switchMap(_linksVideo) { convertToVideoData(it) }

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

    // manage liveData
    fun postVideosPublic() =
        CoroutineScope(Dispatchers.IO).launch {
            _linksVideo.postValue(dao.selectLinksPublicByType(videoType))
        }

    fun postAllVideos() =
        CoroutineScope(Dispatchers.IO).launch {
            _linksVideo.postValue(dao.selectLinksByType(videoType))
        }

    private fun convertToVideoData(dataList: List<SjLinksAndDomainsWithTags>): MutableLiveData<List<VideoData>> {
        val list = mutableListOf<VideoData>()
        for (i in dataList.indices) list.add(VideoData.getVideoDataBy(dataList[i]))
        return MutableLiveData(list)
    }


}
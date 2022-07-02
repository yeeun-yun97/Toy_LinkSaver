package com.github.yeeun_yun97.toy.linksaver.test.repository

import androidx.lifecycle.LiveData
import com.github.yeeun_yun97.toy.linksaver.data.SjTestDataUtil
import com.github.yeeun_yun97.toy.linksaver.data.model.VideoData
import com.github.yeeun_yun97.toy.linksaver.test.SjBaseTest
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

@HiltAndroidTest
class VideoRepositoryTest : SjBaseTest() {
    private lateinit var videos: LiveData<List<VideoData>>

    override fun before() {
        videos = videoRepo.linksVideo
    }

    private fun postVideosPublic() = videoRepo.postVideosPublic()
    private fun postAllVideos() = videoRepo.postAllVideos()

    @Test
    fun listAllVideos() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()
            val result = getValueOrThrow(videos,::postAllVideos)
            Assert.assertEquals(SjTestDataUtil.testVideoLinks.size,result.size)
        }
    }

    @Test
    fun listVideosPublic() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()
            val resultPublic = getValueOrThrow(videos,::postVideosPublic)
            Assert.assertEquals(SjTestDataUtil.testVideoLinksPublic.size,resultPublic.size)
        }
    }


}
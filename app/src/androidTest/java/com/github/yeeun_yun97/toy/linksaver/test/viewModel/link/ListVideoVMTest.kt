package com.github.yeeun_yun97.toy.linksaver.test.viewModel.link

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.yeeun_yun97.toy.linksaver.data.SjTestDataUtil
import com.github.yeeun_yun97.toy.linksaver.test.SjBaseTest
import com.github.yeeun_yun97.toy.linksaver.viewmodel.link.ListVideoViewModel
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ListVideoVMTest : SjBaseTest() {
    private lateinit var viewModel: ListVideoViewModel

    override suspend fun before() {
        viewModel = ListVideoViewModel(application, videoListRepo)
    }

    @Test
    fun setPrivateOnTest(){
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            viewModel.setPrivate(true)
            delay(3000)

            val result = getValueOrThrow(viewModel.videoDatas)
            Assert.assertEquals(SjTestDataUtil.testVideoLinksPublic.size,result.size)
        }
    }

    @Test
    fun setPrivateOffTest(){
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            viewModel.setPrivate(false)
            delay(3000)

            val result = getValueOrThrow(viewModel.videoDatas)
            Assert.assertEquals(SjTestDataUtil.testVideoLinks.size,result.size)
        }
    }


}
package com.github.yeeun_yun97.toy.linksaver.test.repository

import com.github.yeeun_yun97.toy.linksaver.data.SjTestDataUtil
import com.github.yeeun_yun97.toy.linksaver.test.SjBaseTest
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test

@HiltAndroidTest
class LinkRepositoryTest : SjBaseTest() {
    @Test
    fun listAllLinks() {
        testPostingLiveData(linkRepo.links, ::postAllLinks, SjTestDataUtil.testAllLinks.size)
    }

    private fun postAllLinks() = linkRepo.postAllLinks()

    @Test
    fun listLinksPublic() {
        testPostingLiveData(linkRepo.links, ::postLinksPublic, SjTestDataUtil.testLinksPublic.size)
    }

    private fun postLinksPublic() = linkRepo.postLinksPublic()

}
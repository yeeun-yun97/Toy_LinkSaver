package com.github.yeeun_yun97.toy.linksaver.searchTest

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.yeeun_yun97.toy.linksaver.data.db.SjDatabaseUtil
import com.github.yeeun_yun97.toy.linksaver.data.model.ELinkType
import com.github.yeeun_yun97.toy.linksaver.data.model.SjDomain
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLink
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLinksAndDomainsWithTags
import com.github.yeeun_yun97.toy.linksaver.data.repository.getValueOrTimeOut
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.*
import kotlinx.coroutines.*
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeoutException

@RunWith(AndroidJUnit4::class)
class SearchRepositoryTest {
    private lateinit var linkRepo: SjLinkRepository
    private lateinit var domainRepo: SjDomainRepository
    private lateinit var searchSetRepo: SjSearchSetRepository
    private lateinit var tagRepo: SjTagRepository
    private lateinit var videoRepo: SjVideoRepository

    @Before
    fun openDB() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        SjDatabaseUtil.openDatabaseForTest(context)

        this.linkRepo = SjLinkRepository.getInstance()
        this.searchSetRepo = SjSearchSetRepository.getInstance()
        this.domainRepo = SjDomainRepository.getInstance()
    }

    @After
    fun closeDB() {
        SjDatabaseUtil.closeDatabase()
    }

    @Test
    fun listLinks() {
        runBlocking(Dispatchers.Main) {
            val domain = SjDomain(
                did = 1,
                name = "테스트 도메인",
                url = "https://"
            )
            val domainId = domainRepo.insertDomain(domain)

            val link = SjLink(
                name = "네이버",
                did = domainId.toInt(),
                url = "https://m.naver.com/",
                type = ELinkType.link
            )
            val insertJob = linkRepo.insertLinkAndTags(null, link, listOf())
            insertJob.join()

            var result : List<SjLinksAndDomainsWithTags>? = null
            linkRepo.links.observeForever{
                result = it
            }

            // refresh data and wait
            val postJob = linkRepo.postAllLinks()
            postJob.join()
            delay(1500)

            // return or throw
            if(result==null)throw TimeoutException("Live Data has Null Value")
            else Assert.assertEquals(1,result!!.size)
        }
    }


}
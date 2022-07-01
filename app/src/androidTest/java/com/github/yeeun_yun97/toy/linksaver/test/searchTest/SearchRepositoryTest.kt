package com.github.yeeun_yun97.toy.linksaver.test.searchTest

import com.github.yeeun_yun97.toy.linksaver.data.SjTestDataUtil
import com.github.yeeun_yun97.toy.linksaver.data.db.SjDatabase
import com.github.yeeun_yun97.toy.linksaver.data.model.*
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.SjDomainRepository
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.SjLinkRepository
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.SjSearchSetRepository
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.SjTagRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeoutException
import javax.inject.Inject
import javax.inject.Named

@HiltAndroidTest
class SearchRepositoryTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("test_db")
    lateinit var db: SjDatabase

    lateinit var linkRepo: SjLinkRepository
    lateinit var domainRepo: SjDomainRepository
    lateinit var tagRepo: SjTagRepository
    lateinit var searchSetRepo: SjSearchSetRepository

    @Before
    fun init() {
        hiltRule.inject()
        linkRepo = SjLinkRepository(db.getLinkDao())
        domainRepo = SjDomainRepository(db.getDomainDao())
        tagRepo = SjTagRepository(db.getTagDao())
        searchSetRepo = SjSearchSetRepository(db.getSearchSetDao())
    }

    @Test
    fun listLinksAll() {
        runBlocking(Dispatchers.Main) {
            SjTestDataUtil.insertDatas(linkRepo, domainRepo, tagRepo, searchSetRepo)

            var result: List<SjLinksAndDomainsWithTags>? = null
            linkRepo.links.observeForever {
                result = it
            }

            // refresh data and wait
            val postJob = linkRepo.postAllLinks()
            postJob.join()
            delay(1500)

            // return or throw
            if (result == null) throw TimeoutException("Live Data has Null Value")
            else Assert.assertEquals(SjTestDataUtil.testAllLinks.size, result!!.size)
        }
    }

    @Test
    fun listLinksPublic() {
        runBlocking(Dispatchers.Main) {
            SjTestDataUtil.insertDatas(linkRepo, domainRepo, tagRepo, searchSetRepo)

            var result: List<SjLinksAndDomainsWithTags>? = null
            linkRepo.links.observeForever {
                result = it
            }

            // refresh data and wait
            val postJob = linkRepo.postLinksPublic()
            postJob.join()
            delay(1500)

            // return or throw
            if (result == null) throw TimeoutException("Live Data has Null Value")
            else Assert.assertEquals(SjTestDataUtil.testLinksPublic.size, result!!.size)
        }
    }


}
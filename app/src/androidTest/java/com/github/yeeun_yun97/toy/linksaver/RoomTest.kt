package com.github.yeeun_yun97.toy.linksaver

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.yeeun_yun97.toy.linksaver.data.dao.SjDao
import com.github.yeeun_yun97.toy.linksaver.data.db.SjDatabase
import com.github.yeeun_yun97.toy.linksaver.data.model.LinkTagCrossRef
import com.github.yeeun_yun97.toy.linksaver.data.model.SjDomain
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLink
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomTest {
    private lateinit var dao: SjDao

    private val domain = SjDomain(did = 11, name = "Daum", url = "https:www.daum.net/")
    private val link = SjLink(lid = 20, name = "TestLink", did = 10, url = "#")
    private val tag = SjTag(tid = 30, name="TestTag")
    private val crossRef=LinkTagCrossRef(lid=20, tid=30)

    @Before
    fun createDB() {
        val applicationContext = ApplicationProvider.getApplicationContext<Context>()
        //SjDatabase.openDatabase(applicationContext)
        SjDatabase.openDatabaseForTest(applicationContext)
        this.dao = SjDatabase.getDao()
    }

    @After
    fun closeDB() {
        SjDatabase.closeDatabase()
    }

    @Test
    fun insertDomainTest(){
        dao.insertDomain(domain)

        val domainList= dao.getAllDomains()
        assertThat(domainList[0], equalTo(domain))
    }


    @Test
    fun insertTagTest(){
        dao.insertTag(tag)

        val tagList= dao.getAllTags()
        assertThat(tagList[0], equalTo(tag))
    }

    @Test
    fun insertLinkAndDomainWithTags() {
        dao.insertDomain(domain)
        dao.insertTag(tag)
        dao.insertLink(link)
        dao.insertLinkTagCrossRef(crossRef)

        val domains= dao.getAllDomains()
        val linksAndDomain = dao.getLinksAndDomain()
        val linksWithTags = dao.getLinksWithTags()
        val tags = dao.getAllTags()

        assertThat(domains[0],equalTo(domain))

        assertThat(linksAndDomain[0].domain,equalTo(domain))
        assertThat(linksAndDomain[0].link, equalTo(link))

        assertThat(linksWithTags[0].tags[0], equalTo(tag))
        assertThat(linksWithTags[0].link,equalTo(link))

        assertThat(tags[0], equalTo(tag))
    }

}
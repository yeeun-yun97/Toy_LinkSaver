package com.github.yeeun_yun97.toy.linksaver.data.basic

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.yeeun_yun97.toy.linksaver.data.dao.SjDao
import com.github.yeeun_yun97.toy.linksaver.data.db.SjDatabase
import com.github.yeeun_yun97.toy.linksaver.data.model.SjDomain
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLink
import com.github.yeeun_yun97.toy.linksaver.data.model.SjSearch
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
open class RoomDBBasicTest {

    //dao
    protected lateinit var dao: SjDao

    //test model domain
    protected val defaultDomain = SjDomain(did = 1, name = "-", url = "")
    protected val domain = SjDomain(name = "Naver", url = "https://m.naver.com/")
    protected val domain2 = SjDomain(name = "Daum", url = "https://www.Daum.net/")

    //test model link
    protected val link = SjLink( name = "TestLink", did = 0, url = "#1")
    protected val link2 = SjLink( name = "TestLink", did = 0, url = "#2")

    //test model tag
    protected val tag = SjTag( name = "TestTag")
    protected val tag2 = SjTag( name = "TestTag2")

    //test model search
    protected val search = SjSearch(keyword = "TestSearch")
    protected val search2 = SjSearch(keyword = "TestSearch2")

    @Before
    fun openDB() {
        val applicationContext = ApplicationProvider.getApplicationContext<Context>()
        SjDatabase.openDatabaseForTest(applicationContext)

        dao = SjDatabase.getDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        SjDatabase.closeDatabase()
    }

}
package com.github.yeeun_yun97.toy.linksaver.test

import android.app.Application
import androidx.lifecycle.LiveData
import com.github.yeeun_yun97.toy.linksaver.data.SjTestDataUtil
import com.github.yeeun_yun97.toy.linksaver.data.db.SjDatabase
import com.github.yeeun_yun97.toy.linksaver.data.repository.SjDataStoreRepository
import com.github.yeeun_yun97.toy.linksaver.data.repository.SjNetworkRepository
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.*
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.link.SjEditLinkRepository
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.link.SjListLinkRepository
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.link.SjListVideoRepository
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.link.SjViewLinkRepository
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.search.SjSearchSetRepository
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.tag.SjListTagGroupRepository
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.tag.SjViewTagGroupRepository
import dagger.hilt.android.testing.HiltAndroidRule
import kotlinx.coroutines.*
import org.junit.Before
import org.junit.Rule
import java.util.concurrent.TimeoutException
import javax.inject.Inject
import javax.inject.Named

abstract class SjBaseTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("test_db")
    protected lateinit var db: SjDatabase

    @Inject
    lateinit var application: Application

    protected lateinit var linkListRepo: SjListLinkRepository
    protected lateinit var linkRepo: SjViewLinkRepository
    protected lateinit var domainListRepo: SjDomainListRepository
    protected lateinit var tagListRepo: SjListTagGroupRepository
    protected lateinit var tagGroupRepo: SjViewTagGroupRepository
    protected lateinit var searchSetListRepo: SjSearchSetRepository
    protected lateinit var videoListRepo: SjListVideoRepository
    protected lateinit var countRepo: SjCountRepository
    protected lateinit var editLinkRepo: SjEditLinkRepository

    protected lateinit var networkRepo: SjNetworkRepository
    protected lateinit var dataStoreRepo: SjDataStoreRepository



    protected val ERROR_MESSAGE_LIVEDATA_NULL = "LiveData has null value"

    @Before
    fun init() {
        hiltRule.inject()
        networkRepo = SjNetworkRepository.newInstance()
        linkListRepo = SjListLinkRepository(db.getLinkDao())
        linkRepo = SjViewLinkRepository(db.getLinkDao())
        videoListRepo = SjListVideoRepository(db.getLinkDao())
        domainListRepo = SjDomainListRepository(db.getDomainDao())
        tagListRepo = SjListTagGroupRepository(db.getTagDao())
        tagGroupRepo = SjViewTagGroupRepository(db.getTagDao())
        searchSetListRepo = SjSearchSetRepository(db.getSearchSetDao())
        countRepo = SjCountRepository(db.getCountDao(), db.getDomainDao(), db.getTagDao())
        editLinkRepo = SjEditLinkRepository(db.getLinkDao())
        dataStoreRepo = SjDataStoreRepository.getInstance()
        runBlocking(Dispatchers.Main) {
            before()
        }
    }

    protected abstract fun before()

    protected suspend fun insertBaseData() =
        CoroutineScope(Dispatchers.IO).launch {
            SjTestDataUtil.insertDatas(
                editLinkRepo,
                domainListRepo,
                tagListRepo,
                tagGroupRepo,
                searchSetListRepo
            )
        }

    protected suspend fun <T> getValueOrThrow(
        liveData: LiveData<T>,
        postFunction: (() -> Any)? = null,
        timeout: Long = 3000
    ): T {
        var result: T? = null
        liveData.observeForever {
            result = it
        }

        if (postFunction != null) {
            val postJob = postFunction()
            if (postJob is Job) postJob.join()
        }
        delay(timeout)

        if (result == null) throw TimeoutException(ERROR_MESSAGE_LIVEDATA_NULL)
        else return result!!
    }


}
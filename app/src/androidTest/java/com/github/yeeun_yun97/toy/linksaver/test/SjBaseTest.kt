package com.github.yeeun_yun97.toy.linksaver.test

import androidx.lifecycle.LiveData
import androidx.test.core.app.ActivityScenario.launch
import com.github.yeeun_yun97.toy.linksaver.data.SjTestDataUtil
import com.github.yeeun_yun97.toy.linksaver.data.db.SjDatabase
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.*
import dagger.hilt.android.testing.HiltAndroidRule
import kotlinx.coroutines.*
import org.junit.Assert
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
    lateinit var db: SjDatabase

    lateinit var linkRepo: SjLinkRepository
    lateinit var domainRepo: SjDomainRepository
    lateinit var tagRepo: SjTagRepository
    lateinit var searchSetRepo: SjSearchSetRepository
    lateinit var videoRepo: SjVideoRepository

    @Before
    fun init() {
        hiltRule.inject()
        linkRepo = SjLinkRepository(db.getLinkDao())
        videoRepo = SjVideoRepository(db.getLinkDao())
        domainRepo = SjDomainRepository(db.getDomainDao())
        tagRepo = SjTagRepository(db.getTagDao())
        searchSetRepo = SjSearchSetRepository(db.getSearchSetDao())
        before()
    }

    protected abstract fun before()

    protected suspend fun insertBaseData() =
        CoroutineScope(Dispatchers.IO).launch {
            SjTestDataUtil.insertDatas(
                linkRepo,
                domainRepo,
                tagRepo,
                searchSetRepo
            )
        }

    protected suspend fun <T> getValueOrThrow(
        liveData: LiveData<T>,
        postFunction: (() -> Any),
        timeout: Long = 3000
    ): T {
        var result: T? = null
        liveData.observeForever {
            result = it
        }

        val postJob = postFunction()
        if (postJob is Job) postJob.join()
        delay(timeout)

        if (result == null) throw TimeoutException("LiveData has null value")
        else return result!!
    }


}
package com.github.yeeun_yun97.toy.linksaver.test.repository

import androidx.lifecycle.LiveData
import com.github.yeeun_yun97.toy.linksaver.test.SjBaseTest
import kotlinx.coroutines.Job

abstract class SjRepositoryBaseTest<T> : SjBaseTest() {
    private lateinit var targetLiveData: LiveData<List<T>>

    override fun before() { targetLiveData = targetLiveData() }
    abstract fun targetLiveData():LiveData<List<T>>

    protected fun assertLiveDataUpdatedSize(
        postFunction: () -> Job,
        expectedSize: Int,
        timeout: Long = 1500
    ){
        testPostingLiveData(
            targetLiveData,
            postFunction,
            expectedSize,
            timeout
        )
    }










}
package com.github.yeeun_yun97.toy.linksaver.test.viewModel.setting

import com.github.yeeun_yun97.toy.linksaver.test.SjBaseTest
import com.github.yeeun_yun97.toy.linksaver.viewmodel.androidViewModels.LockViewModel
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

@HiltAndroidTest
class LockVMTest : SjBaseTest() {
    private lateinit var viewModel: LockViewModel

    private val password = listOf(1, 2, 3, 4, 5, 6)
    private val wrong = listOf(3, 4, 2, 6, 3, 4)
    val passwordStr = StringBuilder().apply {
        for (num in password)
            append(num)
    }.toString()

    override suspend fun before() {
        viewModel = LockViewModel(application, dataStoreRepo)
    }

    @Test
    fun setPasswordAndSuccessTest() {
        runBlocking(Dispatchers.Main) {
            viewModel.setPassword(passwordStr).join()

            for (num in password)
                viewModel.appendNumber(num)
            delay(3000)

            val result = getValueOrThrow(viewModel.isPasswordCorrect)
            Assert.assertEquals(true, result)
        }
    }

    @Test
    fun setPasswordAndFailureTest() {
        runBlocking(Dispatchers.Main) {
            viewModel.setPassword(passwordStr).join()

            for (num in wrong)
                viewModel.appendNumber(num)
            delay(3000)

            val result = getValueOrThrow(viewModel.isPasswordCorrect)
            Assert.assertEquals(false, result)
        }
    }


}
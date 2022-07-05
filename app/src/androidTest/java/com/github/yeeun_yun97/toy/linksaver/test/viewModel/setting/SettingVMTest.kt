package com.github.yeeun_yun97.toy.linksaver.test.viewModel.setting

import com.github.yeeun_yun97.toy.linksaver.test.SjBaseTest
import com.github.yeeun_yun97.toy.linksaver.viewmodel.setting.SettingViewModel
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

@HiltAndroidTest
class SettingVMTest : SjBaseTest() {
    private lateinit var viewModel: SettingViewModel

    override fun before() {
        viewModel = SettingViewModel(application,dataStoreRepo)
    }

    @Test
    fun setPrivateModeOn(){
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            val isPrivateMode = true
            viewModel.setPrivateMode(isPrivateMode).join()

            val result = getValueOrThrow(viewModel.isPrivateMode)
            Assert.assertEquals(isPrivateMode,result)
        }
    }

    @Test
    fun setPrivateModeOff(){
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            val isPrivateMode = false
            viewModel.setPrivateMode(isPrivateMode).join()

            val result = getValueOrThrow(viewModel.isPrivateMode)
            Assert.assertEquals(isPrivateMode,result)
        }
    }

    @Test
    fun checkPasswordNotSet(){
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            val password = viewModel.passwordFlow.first()
            Assert.assertEquals(true,password.isEmpty())
        }
    }


}
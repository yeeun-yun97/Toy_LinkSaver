package com.github.yeeun_yun97.toy.linksaver.test.viewModel.android

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.yeeun_yun97.toy.linksaver.test.SjBaseTest
import com.github.yeeun_yun97.toy.linksaver.ui.activity.MainActivity
import com.github.yeeun_yun97.toy.linksaver.viewmodel.app.SettingViewModel
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

//@HiltAndroidTest
//@RunWith(AndroidJUnit4::class)
//class SettingViewModelTest : SjBaseTest() {

//    @get:Rule
//    var activityRule: ActivityScenarioRule<MainActivity> =
//        ActivityScenarioRule(MainActivity::class.java)
//
//    @Inject // inject not working here!! T
//    private lateinit var viewModel: SettingViewModel
//
//    override fun before() {
//        viewModel
//    }
//
//    @Test
//    fun setPrivateModeOn(){
//        runBlocking(Dispatchers.Main) {
//            insertBaseData().join()
//
//            val isPrivateMode = true
//            viewModel.setPrivateMode(isPrivateMode).join()
//
//            val result = getValueOrThrow(viewModel.isPrivateMode)
//            Assert.assertEquals(isPrivateMode,result)
//        }
//    }
//
//    @Test
//    fun setPrivateModeOff(){
//        runBlocking(Dispatchers.Main) {
//            insertBaseData().join()
//
//            val isPrivateMode = false
//            viewModel.setPrivateMode(isPrivateMode).join()
//
//            val result = getValueOrThrow(viewModel.isPrivateMode)
//            Assert.assertEquals(isPrivateMode,result)
//        }
//    }
//
//    @Test
//    fun checkPasswordNotSet(){
//        runBlocking(Dispatchers.Main) {
//            insertBaseData().join()
//
//            val password = viewModel.passwordFlow.first()
//            Assert.assertEquals(true,password.isEmpty())
//        }
//    }

//}
package com.github.yeeun_yun97.toy.linksaver.test.ui

import android.app.Activity
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
abstract class SjUiBaseTest<T:Activity>{
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var activityRule: ActivityScenarioRule<T> = getScenarioRule()

    abstract fun getScenarioRule() : ActivityScenarioRule<T>

    @Before
    fun setup(){ hiltRule.inject()}

    protected fun sleep(timeout:Long = 500){
        Thread.sleep(timeout)
    }

}
package com.github.yeeun_yun97.toy.linksaver.test.ui.main

import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.yeeun_yun97.toy.linksaver.test.ui.SjUiBaseTest
import com.github.yeeun_yun97.toy.linksaver.ui.activity.MainActivity

open class SjUiMainActivityTest : SjUiBaseTest<MainActivity>() {
    override fun getScenarioRule(): ActivityScenarioRule<MainActivity> {
        return ActivityScenarioRule(MainActivity::class.java)
    }
}
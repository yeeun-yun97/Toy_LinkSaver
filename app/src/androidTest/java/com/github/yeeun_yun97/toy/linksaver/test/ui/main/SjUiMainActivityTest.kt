package com.github.yeeun_yun97.toy.linksaver.test.ui.main

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.test.ui.SjUiBaseTest
import com.github.yeeun_yun97.toy.linksaver.ui.activity.MainActivity

open class SjUiMainActivityTest : SjUiBaseTest<MainActivity>() {
    override fun getScenarioRule(): ActivityScenarioRule<MainActivity> {
        return ActivityScenarioRule(MainActivity::class.java)
    }

    protected fun openToolBarMenuAndClick(menuName:String) {
        onView(ViewMatchers.withId(R.id.toolbar)).perform(ViewActions.click())
        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext())
        onView(ViewMatchers.withText(menuName)).perform(ViewActions.click())
    }
}
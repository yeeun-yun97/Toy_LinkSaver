package com.github.yeeun_yun97.toy.linksaver.test.ui.main.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.test.ui.main.SjUiMainActivityTest
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test

@HiltAndroidTest
class MainActivityTest : SjUiMainActivityTest() {
    @Test
    fun bottomNavigationTest() {
        onView(withId(R.id.settingItem)).perform(click())
        onView(withId(R.id.settingRecyclerView)).check(matches(isDisplayed()))

        onView(withId(R.id.videoItem)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.emptyImageView)).check(matches(isDisplayed()))

        onView(withId(R.id.linkItem)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.emptyImageView)).check(matches(isDisplayed()))
    }
}
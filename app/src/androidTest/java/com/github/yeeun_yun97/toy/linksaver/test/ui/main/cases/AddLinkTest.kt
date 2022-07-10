package com.github.yeeun_yun97.toy.linksaver.test.ui.main.cases

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.test.ui.main.SjUiMainActivityTest
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class AddLinkTest : SjUiMainActivityTest() {
    @Inject
    lateinit var application: Application

    private val label = "구글"
    private val url = "https://www.google.com/"

    @Before
    fun copyUrl() {
        val clipboard =
            application.applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, url)
        clipboard.setPrimaryClip(clip)
    }

    @Test
    fun addLinkTest() {
        // start creating
        onView(withId(R.id.floatingActionView)).perform(click())
        onView(withId(R.id.pasteImageView)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.urlTextView)).check(matches(withText(url)))
        onView(withId(R.id.nextButton)).perform(click())

        // edit
        Thread.sleep(1000)
        onView(withId(R.id.nameEditText)).check(matches(isDisplayed()))
        onView(withId(R.id.nameEditText)).perform(click())
        onView(withId(R.id.nameEditText)).perform(replaceText(label))

        // save
        onView(withId(R.id.menu_save)).perform(click())

        // list check
        Thread.sleep(1000)
        onView(withText(label)).check(matches(isDisplayed()))

        // detail check
        onView(withText(label)).perform(click())
        onView(withText(url)).check(matches(isDisplayed()))
        onView(withText(label)).check(matches(isDisplayed()))

        // delete
        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext())
        onView(withText("삭제하기")).perform(click())

        // list check
        onView(withText(label)).check(doesNotExist())

    }


}
package com.github.yeeun_yun97.toy.linksaver.test.ui.main.cases

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
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
    lateinit var application : Application

    private val label = "구글"
    private val url = "https://www.google.com/"

    @Before
    fun copyUrl(){
        val clipboard = application.applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label,url)
        clipboard.setPrimaryClip(clip)
    }

    @Test
    fun addLinkTest(){
        onView(withId(R.id.floatingActionView)).perform(click())
        onView(withId(R.id.pasteImageView)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.urlTextView)).check(matches(withText(url)))
        onView(withId(R.id.nextButton)).perform(click())




//        onView(withId(R.id.toolbar)).perform(click())
//        openActionBarOverflowOrOptionsMenu(application.applicationContext)
//        onView(withText("save")).perform(click())
//        onView(withId(R.id.menu_save)).perform(click())

//        clickMenu(R.id.menu_save)
//        onView(withText("save")).check(matches(isDisplayed()))


        Thread.sleep(1000)
        onView(withId(R.id.nameEditText)).check(matches(isDisplayed()))
//        onView(withId(R.id.nameEditText)).perform(scrollTo(),click())
//        Thread.sleep(1000)
//        onView(withId(R.id.nameEditText)).perform(replaceText(label))
//        onView(withId(R.id.nameEditText)).perform(typeText(label))
//        onView(withId(R.id.menu_save)).perform(click())
    }


}
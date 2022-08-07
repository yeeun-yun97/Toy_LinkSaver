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
    private val updateLabel = "*쿨한 링크 이름*"
    private val url = "https://www.google.com/"

    @Before
    fun copyUrl() {
        val clipboard =
            application.applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, url)
        clipboard.setPrimaryClip(clip)
    }

    @Test
    fun linkLifeCycleTest() {
        pasteUrl()
        onView(withId(R.id.urlTextView)).check(matches(withText(url)))

        /* move to editLink */onView(withId(R.id.nextButton)).perform(click())
        sleep()
        onView(withId(R.id.urlTextView)).check(matches(withText(url)))
        setLinkName(label)

        /* move to listLink */onView(withId(R.id.menu_save)).perform(click())
        sleep(1000)
        onView(withText(label)).check(matches(isDisplayed()))

        /* move to detailLink */onView(withText(label)).perform(click())
        onView(withText(url)).check(matches(isDisplayed()))
        onView(withText(label)).check(matches(isDisplayed()))

        /* move to listLink */deleteLink()
        sleep()
        onView(withText(label)).check(doesNotExist())
    }

    @Test
    fun editLinkTest() {
        pasteUrl()

        /* move to editLink */onView(withId(R.id.nextButton)).perform(click())
        sleep()
        setLinkName(label)

        /* move to listLink */onView(withId(R.id.menu_save)).perform(click())
        sleep(1000)

        /* move to detailLink */onView(withText(label)).perform(click())
        sleep()
        editLink(updateLabel)

        /* move to listLink */onView(withId(R.id.menu_save)).perform(click())
        sleep(1000)
        onView(withId(R.id.nameTextView)).check(matches(withText(updateLabel)))
        deleteLink()
    }


    private fun pasteUrl() {
        onView(withId(R.id.floatingActionView)).perform(click())
        onView(withId(R.id.pasteImageView)).perform(click())
    }

    private fun setLinkName(linkName: String) {
        onView(withId(R.id.nameEditText)).perform(click())
        onView(withId(R.id.nameEditText)).perform(replaceText(linkName))
    }

    private fun deleteLink() {
        openToolBarMenuAndClick("삭제하기")
    }

    private fun editLink(updateName: String) {
        openToolBarMenuAndClick("수정하기")
        setLinkName(updateName)
    }


}
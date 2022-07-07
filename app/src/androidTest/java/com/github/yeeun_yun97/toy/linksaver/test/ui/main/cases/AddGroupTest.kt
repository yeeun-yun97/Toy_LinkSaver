package com.github.yeeun_yun97.toy.linksaver.test.ui.main.cases

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.matcher.ClickCloseIconAction
import com.github.yeeun_yun97.toy.linksaver.test.ui.main.SjUiMainActivityTest
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.allOf
import org.junit.Test

@HiltAndroidTest
class AddGroupTest : SjUiMainActivityTest(){
    private val groupName = "새 그룹"
    private val editGroupName = "더 멋진 이름"
    private val tagName = "새 태그"

    companion object{
        private fun createGroup(groupName:String){
            onView(withId(R.id.settingItem)).perform(click())
            onView(withText("태그 그룹 목록")).perform(click())

            onView(withId(R.id.toolbar)).perform(click())
            openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext())
            onView(withText("새 그룹 추가하기")).perform(click())

            onView(withId(R.id.groupNameEditText)).perform(replaceText(groupName))
            onView(withText("확인")).perform(click())
        }

        private fun renameGroup(updateName:String){
            onView(withId(R.id.swapImageView)).perform(click())
            onView(withText("그룹 이름 바꾸기")).perform(click())

            onView(withId(R.id.groupNameEditText)).perform(replaceText(updateName))
            onView(withText("확인")).perform(click())
        }

        private fun deleteGroup(groupName:String){
            onView(allOf(withId(R.id.swapImageView),hasSibling(withText(groupName)))).perform(click())
            onView(withText("그룹 삭제하기")).perform(click())
        }

        private fun addNewTagInGroup(tagName:String){
            onView(allOf(withId(R.id.swapImageView))).perform(click())
            onView(withText("그룹 내 태그 관리하기")).perform(click())

            onView(withId(R.id.toolbar)).perform(click())
            openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext())
            onView(withText("그룹에 새 태그 추가하기")).perform(click())

            onView(withId(R.id.tagNameTextView)).perform(replaceText(tagName))
            onView(withText("확인")).perform(click())
        }

        fun deleteTagInGroup(tagName:String){
            onView(withText(tagName)).perform(ClickCloseIconAction())
        }
    }

    @Test
    fun groupLifecycleTest(){
        createGroup(groupName)
        onView(withText(groupName)).check(matches(isDisplayed()))
        onView(withText("비어 있는 그룹입니다.")).check(matches(isDisplayed()))

        sleep()
        renameGroup(editGroupName)
        onView(withText(editGroupName)).check(matches(isDisplayed()))

        sleep()
        deleteGroup(editGroupName)
        onView(withText(editGroupName)).check(doesNotExist())
    }


    @Test
    fun groupEditTagsTest(){
        createGroup(groupName)

        sleep()
        sleep()
        addNewTagInGroup(tagName)
        onView(withText(tagName)).check(matches(isDisplayed()))

        sleep()
        deleteTagInGroup(tagName)
        onView(withText(tagName)).check(doesNotExist())

        sleep()
        deleteGroup(groupName)
    }

}
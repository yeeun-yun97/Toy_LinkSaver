package com.github.yeeun_yun97.toy.linksaver.matcher

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import com.google.android.material.chip.Chip
import org.hamcrest.Matcher

//https://stackoverflow.com/questions/54811112/using-espresso-to-run-ui-tests-how-do-you-reference-the-close-icon-in-a-materia
class ClickCloseIconAction : ViewAction {

    override fun getConstraints(): Matcher<View> {
        return ViewMatchers.isAssignableFrom(Chip::class.java)
    }

    override fun getDescription(): String {
        return "click drawable "
    }

    override fun perform(uiController: UiController, view: View) {
        val chip = view as Chip//we matched
        chip.performCloseIconClick()
    }
}
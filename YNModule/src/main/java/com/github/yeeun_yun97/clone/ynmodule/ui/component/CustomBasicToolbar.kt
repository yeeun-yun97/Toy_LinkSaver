package com.github.yeeun_yun97.clone.ynmodule.ui.component

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.github.yeeun_yun97.clone.ynmodule.R
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar

class CustomBasicToolbar(context: Context, attrs: AttributeSet?) : AppBarLayout(context, attrs) {

    var toolbarTitle = ""
        set(value) {
            field = value
            toolbar.setTitle(value)
            invalidate()
            requestLayout()
        }
    private val toolbar: MaterialToolbar = MaterialToolbar(context)

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomBasicToolbar,
            0, 0
        ).apply {
            try {
                toolbarTitle = getString(R.styleable.CustomBasicToolbar_toolbarTitle).toString()
            } finally {
                recycle()
            }
        }

        toolbar.layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, //width
            ViewGroup.LayoutParams.WRAP_CONTENT  //height
        )

        toolbar.setBackgroundColor(context.resources.getColor(R.color.white, context.theme))
        toolbar.setTitleTextColor(context.resources.getColor(R.color.charcoal, context.theme))
        this.addView(toolbar)
    }

}
package com.github.yeeun_yun97.clone.ynmodule.ui.component

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.annotation.MenuRes
import androidx.core.content.res.ResourcesCompat
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
        CustomComponentStyleUtil.setMaterialCustomToolbarStyle(toolbar)
        this.addView(toolbar)
    }

    fun setMenu(@MenuRes menu: Int, handlerMap: HashMap<Int, () -> Unit>) {
        toolbar.inflateMenu(menu)
        toolbar.setOnMenuItemClickListener { menuItem -> handleMenuClick(handlerMap[menuItem.itemId]) }
    }

    private fun handleMenuClick(function: (() -> Unit)?): Boolean {
        return if (function == null) {
            false
        } else {
            function()
            true
        }
    }

}
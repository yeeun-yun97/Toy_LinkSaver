package com.github.yeeun_yun97.toy.linksaver.ui.activity.basic

import androidx.viewbinding.ViewBinding
import com.github.yeeun_yun97.clone.ynmodule.ui.activity.ViewBindingBasicActivity
import com.github.yeeun_yun97.toy.linksaver.R

abstract class SjBasicActivity<T : ViewBinding> : ViewBindingBasicActivity<T>() {
    protected val RESULT_SUCCESS = 0
    protected val RESULT_FAILED = 1
    protected val RESULT_CANCELED = 2

    override fun fragmentContainer(): Int = R.id.fragmentContainer
}
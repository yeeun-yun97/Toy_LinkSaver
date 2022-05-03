package com.github.yeeun_yun97.toy.linksaver.ui.activity.basic

import androidx.viewbinding.ViewBinding
import com.github.yeeun_yun97.clone.ynmodule.ui.activity.ViewBindingBasicActivity
import com.github.yeeun_yun97.toy.linksaver.R

abstract class SjBasicActivity<T : ViewBinding> : ViewBindingBasicActivity<T>() {
    override fun fragmentContainer(): Int = R.id.fragmentContainer
}
package com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic

import androidx.databinding.ViewDataBinding
import com.github.yeeun_yun97.clone.ynmodule.ui.fragment.DataBindingBasicFragment
import com.github.yeeun_yun97.toy.linksaver.R

abstract class SjBasicFragment<T : ViewDataBinding> : DataBindingBasicFragment<T>() {

    override fun fragmentContainer(): Int = R.id.fragmentContainer

}
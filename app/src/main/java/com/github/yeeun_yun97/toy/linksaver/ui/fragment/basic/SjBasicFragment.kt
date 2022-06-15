package com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic

import androidx.databinding.ViewDataBinding
import com.github.yeeun_yun97.clone.ynmodule.ui.fragment.DataBindingBasicFragment
import com.github.yeeun_yun97.toy.linksaver.R

abstract class SjBasicFragment<T : ViewDataBinding> : DataBindingBasicFragment<T>() {
    protected val RESULT_SUCCESS = 0
    protected val RESULT_FAILED = 1
    protected val RESULT_CANCELED = 2

    override fun fragmentContainer(): Int = R.id.fragmentContainer

}
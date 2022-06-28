package com.github.yeeun_yun97.toy.linksaver.ui.activity.basic

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.viewbinding.ViewBinding
import com.github.yeeun_yun97.clone.ynmodule.ui.activity.ViewBindingBasicActivity
import com.github.yeeun_yun97.toy.linksaver.R
import org.hamcrest.core.AnyOf

abstract class SjBasicActivity<T : ViewBinding> : ViewBindingBasicActivity<T>() {
    protected val RESULT_SUCCESS = 0
    protected val RESULT_FAILED = 1
    protected val RESULT_CANCELED = 2

    override fun fragmentContainer(): Int = R.id.fragmentContainer

    protected fun replaceFragmentByKoin(cl: Class<out   Fragment>) {
        val fragments = supportFragmentManager.fragments
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        supportFragmentManager.commit {
            for (fragment in fragments) {
                remove(fragment)
            }
            replace(fragmentContainer(), cl, null)
            setReorderingAllowed(true)
        }
    }
}
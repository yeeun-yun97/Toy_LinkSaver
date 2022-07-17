package com.github.yeeun_yun97.toy.linksaver.ui.activity.basic

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.viewbinding.ViewBinding
import com.github.yeeun_yun97.clone.ynmodule.ui.activity.YnMainBaseActivity
import com.github.yeeun_yun97.toy.linksaver.R

abstract class SjMainBasicActivity<T: ViewBinding> : YnMainBaseActivity<T>()  {

    /** 처음에 기본으로 fragmentContainer에 부착할 프래그먼트를 반환. */
    abstract fun homeFragment(): Fragment

    /** onCreate()에서 해야 될 일 있으면 여기에서. */
    protected abstract fun onCreate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        replaceFragmentTo(homeFragment())
        onCreate()
    }

    private fun fragmentContainer(): Int = R.id.fragmentContainer

    // change fragment
    protected fun moveToFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            replace(fragmentContainer(), fragment)
            setReorderingAllowed(true)
            val name = fragment.javaClass.name
            addToBackStack(name)
        }
    }

    protected fun replaceFragmentTo(fragment: Fragment) {
        val fragments = supportFragmentManager.fragments
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        supportFragmentManager.commit {
            for (fragment in fragments) {
                remove(fragment)
            }
            replace(fragmentContainer(), fragment)
            setReorderingAllowed(true)
        }
    }


}

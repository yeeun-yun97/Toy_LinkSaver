package com.github.yeeun_yun97.toy.linksaver.ui.activity.basic

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.viewbinding.ViewBinding
import com.github.yeeun_yun97.toy.linksaver.R

abstract class ViewBindingBasicActivity<T : ViewBinding> : AppCompatActivity() {
    // view binding
    protected lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = viewBindingInflate(layoutInflater)
        setContentView(binding.root)
        replaceFragmentTo(homeFragment())
        onCreate()
    }


    // abstract methods
    /**
     * 여기서 T.inflate(inflater) 호출할 것.
     */
    abstract fun viewBindingInflate(inflater: LayoutInflater): T
    /**
     * onCreate()에서 해야 될 일 있으면 여기에서.
     */
    protected abstract fun onCreate()
    /**
     * 처음에 기본으로 fragmentContainer에 부착할 프래그먼트를 반환.
     */
    abstract fun homeFragment(): Fragment


    // change fragment
    protected fun moveToFragment(fragment: Fragment){
        supportFragmentManager.commit {
            replace(R.id.fragmentContainer, fragment)
            setReorderingAllowed(true)
            addToBackStack(fragment.javaClass.name)
        }
    }
    protected fun replaceFragmentTo(fragment:Fragment) {
        supportFragmentManager.commit {
            replace(R.id.fragmentContainer, fragment)
            setReorderingAllowed(true)
        }
    }

}
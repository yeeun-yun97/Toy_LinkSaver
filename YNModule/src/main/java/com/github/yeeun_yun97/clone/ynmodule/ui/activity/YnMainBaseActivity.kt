package com.github.yeeun_yun97.clone.ynmodule.ui.activity

import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

abstract class YnMainBaseActivity<B : ViewBinding> : ViewBindingBasicActivity<B>() {
    abstract fun getBottomNaviHandlerMap(): Map<Int, () -> Unit>
    abstract fun getBottomNavi(): BottomNavigationView

    override fun onCreate() {
        getBottomNavi().setOnItemSelectedListener {
            val handlerMap = getBottomNaviHandlerMap()
            if (handlerMap.containsKey(it.itemId)) {
                handlerMap[it.itemId]!!.invoke()
                true
            } else {
                false
            }
        }
    }


}
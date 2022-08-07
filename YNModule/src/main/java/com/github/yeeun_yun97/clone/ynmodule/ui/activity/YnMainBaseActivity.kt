package com.github.yeeun_yun97.clone.ynmodule.ui.activity

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

abstract class YnMainBaseActivity<B : ViewBinding> : YnBaseActivity<B>() {
    abstract fun getBottomNaviHandlerMap(): Map<Int, () -> Unit>
    abstract fun getBottomNavi(): BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
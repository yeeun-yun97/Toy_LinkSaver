package com.github.yeeun_yun97.clone.ynmodule.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

import androidx.viewbinding.ViewBinding

abstract class YnBaseActivity<T : ViewBinding> : AppCompatActivity() {
    // view binding
    protected lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = viewBindingInflate(layoutInflater)
        setContentView(binding.root)
    }

    // abstract methods
    /** 여기서 T.inflate(inflater) 호출할 것. */
    abstract fun viewBindingInflate(inflater: LayoutInflater): T


}
package com.github.yeeun_yun97.toy.linksaver.ui.activity

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.github.yeeun_yun97.toy.linksaver.databinding.ActivityViewLinkBinding
import com.github.yeeun_yun97.toy.linksaver.ui.activity.basic.ViewBindingBasicActivity
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.ViewLinkFragment

class ViewLinkActivity : ViewBindingBasicActivity<ActivityViewLinkBinding>() {
    override fun viewBindingInflate(inflater: LayoutInflater): ActivityViewLinkBinding
    = ActivityViewLinkBinding.inflate(inflater)

    override fun homeFragment(): Fragment = ViewLinkFragment()
}
package com.github.yeeun_yun97.toy.linksaver.ui.activity

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.github.yeeun_yun97.toy.linksaver.databinding.ActivityEditLinkBinding
import com.github.yeeun_yun97.toy.linksaver.ui.activity.basic.ViewBindingBasicActivity
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.EditLinkFragment


class EditLinkActivity : ViewBindingBasicActivity<ActivityEditLinkBinding>() {
    override fun viewBindingInflate(inflater: LayoutInflater): ActivityEditLinkBinding =
        ActivityEditLinkBinding.inflate(layoutInflater)

    override fun homeFragment(): Fragment = EditLinkFragment()

}
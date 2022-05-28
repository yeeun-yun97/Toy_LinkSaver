package com.github.yeeun_yun97.toy.linksaver.ui.activity

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.github.yeeun_yun97.toy.linksaver.databinding.ActivityEditLinkBinding
import com.github.yeeun_yun97.toy.linksaver.ui.activity.basic.SjBasicActivity
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.EditPasteFragment
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.EditVideoFragment

class EditLinkActivity : SjBasicActivity<ActivityEditLinkBinding>() {


    override fun viewBindingInflate(inflater: LayoutInflater): ActivityEditLinkBinding =
        ActivityEditLinkBinding.inflate(layoutInflater)

    override fun homeFragment(): Fragment {
        val url = intent.getStringExtra("url")
        return if (url.isNullOrEmpty()) {
            EditPasteFragment()
        } else {
            EditVideoFragment.newInstance(url)
        }
    }

    override fun onCreate() {}
//    override fun homeFragment(): Fragment = EditPasteFragment()


}
package com.github.yeeun_yun97.toy.linksaver.ui.activity

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.github.yeeun_yun97.toy.linksaver.databinding.ActivityEditLinkBinding
import com.github.yeeun_yun97.toy.linksaver.ui.activity.basic.SjBasicActivity
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.edit_link.EditLinkPasteFragment
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.edit_link.EditLinkAndVideoFragment

class EditLinkActivity : SjBasicActivity<ActivityEditLinkBinding>() {


    override fun viewBindingInflate(inflater: LayoutInflater): ActivityEditLinkBinding =
        ActivityEditLinkBinding.inflate(layoutInflater)

    override fun homeFragment(): Fragment {
        val lid = intent.getIntExtra("lid", -1)
        val url = intent.getStringExtra("url") ?: ""
        return if (lid == -1 && url.isEmpty()) {
            EditLinkPasteFragment()
        } else {
            EditLinkAndVideoFragment.newInstance(lid, url)
        }
    }

    override fun onCreate() {}
//    override fun homeFragment(): Fragment = EditPasteFragment()


}
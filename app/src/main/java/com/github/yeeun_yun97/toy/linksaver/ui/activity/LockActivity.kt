package com.github.yeeun_yun97.toy.linksaver.ui.activity

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.github.yeeun_yun97.toy.linksaver.databinding.ActivityPlainBinding
import com.github.yeeun_yun97.toy.linksaver.ui.activity.basic.SjBasicActivity
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.lock.LockFragment

class LockActivity : SjBasicActivity<ActivityPlainBinding>() {

    private val lockFragment = LockFragment()

    override fun viewBindingInflate(inflater: LayoutInflater): ActivityPlainBinding =
        ActivityPlainBinding.inflate(inflater)

    override fun onCreate() {
    }

    override fun homeFragment(): Fragment {
        return lockFragment
    }
}
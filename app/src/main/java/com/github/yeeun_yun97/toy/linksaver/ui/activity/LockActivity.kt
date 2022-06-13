package com.github.yeeun_yun97.toy.linksaver.ui.activity

import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.github.yeeun_yun97.toy.linksaver.databinding.ActivityPlainBinding
import com.github.yeeun_yun97.toy.linksaver.ui.activity.basic.SjBasicActivity
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.lock.LockFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.lock.LockViewModel


class LockActivity : SjBasicActivity<ActivityPlainBinding>() {
    private val viewModel: LockViewModel by viewModels()
    private val lockFragment = LockFragment()

    override fun viewBindingInflate(inflater: LayoutInflater): ActivityPlainBinding =
        ActivityPlainBinding.inflate(inflater)

    override fun onCreate() {
        viewModel.isPasswordCorrect.observe(this, {
            if (it != null) {
                val result = if (it) RESULT_SUCCESS else RESULT_FAILED
                setResult(result)
               this.finish()
            }
        })

    }

    override fun onBackPressed() {
        setResult(RESULT_CANCELED)
        super.onBackPressed()
    }

    override fun homeFragment(): Fragment {
        return lockFragment
    }
}
package com.github.yeeun_yun97.toy.linksaver.ui.activity

import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.github.yeeun_yun97.toy.linksaver.application.RESULT_FAILED
import com.github.yeeun_yun97.toy.linksaver.application.RESULT_SUCCESS
import com.github.yeeun_yun97.toy.linksaver.databinding.ActivityPlainBinding
import com.github.yeeun_yun97.toy.linksaver.ui.activity.basic.SjBasicActivity
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.lock.LockFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.androidViewModels.LockViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LockActivity : SjBasicActivity<ActivityPlainBinding>() {
    private val lockViewModel: LockViewModel by viewModels()
    @Inject lateinit var lockFragment: LockFragment

    override fun viewBindingInflate(inflater: LayoutInflater): ActivityPlainBinding =
        ActivityPlainBinding.inflate(inflater)

    override fun homeFragment(): Fragment {
        return lockFragment
    }

    override fun onCreate() {
        lockViewModel.isPasswordCorrect.observe(this) {
            if (it != null) {
                val result = if (it) RESULT_SUCCESS else RESULT_FAILED
                setResult(result)
                this.finish()
            }
        }
    }

    override fun onBackPressed() {
        setResult(com.github.yeeun_yun97.toy.linksaver.application.RESULT_CANCELED)
        super.onBackPressed()
    }


}
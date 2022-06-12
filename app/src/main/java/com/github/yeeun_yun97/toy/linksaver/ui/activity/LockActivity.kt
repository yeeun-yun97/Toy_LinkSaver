package com.github.yeeun_yun97.toy.linksaver.ui.activity

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
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
        viewModel.bindingPassword1.observe(this, {
            if (it.isNotEmpty())
                Log.d("같은 뷰모델인지 확인하기", "맞음!!")
        })
        viewModel.isPasswordCorrect.observe(this, {
            if (it != null) {
                val result = if (it) RESULT_SUCCESS else RESULT_FAILED
                setResult(result)
                Log.d("끝내기가 왜 안돌지??", "끝내기!!")
               this.finish()
            }
        })

    }

    override fun homeFragment(): Fragment {
        return lockFragment
    }
}
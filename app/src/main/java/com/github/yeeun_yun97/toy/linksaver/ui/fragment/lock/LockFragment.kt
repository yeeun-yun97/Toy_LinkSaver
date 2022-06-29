package com.github.yeeun_yun97.toy.linksaver.ui.fragment.lock

import androidx.fragment.app.activityViewModels
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentLockBinding
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.lock.LockViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LockFragment @Inject constructor() : SjBasicFragment<FragmentLockBinding>() {
    private val viewModel: LockViewModel by activityViewModels()

    override fun layoutId(): Int = R.layout.fragment_lock

    override fun onCreateView() {
        binding.viewModel = viewModel
    }


}
package com.github.yeeun_yun97.toy.linksaver.ui.fragment.lock

import androidx.fragment.app.viewModels
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentLockBinding
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.lock.LockViewModel

class LockFragment : SjBasicFragment<FragmentLockBinding>() {
    private val viewModel: LockViewModel by viewModels()

    override fun layoutId(): Int = R.layout.fragment_lock

    override fun onCreateView() {
        binding.viewModel = viewModel

        binding.zeroImageView.setOnClickListener { viewModel.appendNumber(0) }
        binding.oneImageView.setOnClickListener { viewModel.appendNumber(1) }
        binding.twoImageView.setOnClickListener { viewModel.appendNumber(2) }
        binding.threeImageView.setOnClickListener { viewModel.appendNumber(3) }
        binding.fourImageView.setOnClickListener { viewModel.appendNumber(4) }
        binding.fiveImageView.setOnClickListener { viewModel.appendNumber(5) }
        binding.sixImageView.setOnClickListener { viewModel.appendNumber(6) }
        binding.sevenImageView.setOnClickListener { viewModel.appendNumber(7) }
        binding.eightImageView.setOnClickListener { viewModel.appendNumber(8) }
        binding.nineImageView.setOnClickListener { viewModel.appendNumber(9) }
        binding.cancelTextView.setOnClickListener { viewModel.clearNumbers() }

    }
}
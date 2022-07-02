package com.github.yeeun_yun97.toy.linksaver.ui.fragment.main.setting.personal


import android.widget.Toast
import androidx.fragment.app.viewModels
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentLockBinding
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.LockViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChangePasswordFragment @Inject constructor() : SjBasicFragment<FragmentLockBinding>() {
    private val viewModel: LockViewModel by viewModels()

    private var password: String? = null

    override fun layoutId(): Int = R.layout.fragment_lock
    override fun onCreateView() {
        binding.viewModel = viewModel
        binding.passwordTextView.setText("새로운 비밀번호")

        viewModel.password.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                if (password.isNullOrEmpty()) {
                    handleFirstInput(it)
                } else {
                    handleSecondInput(it)
                }
            }
        }
    }

    private fun handleFirstInput(input: String) {
        password = input
        binding.passwordTextView.setText("새로운 비밀번호 확인")
    }

    private fun handleSecondInput(input: String) {
        if (password == input) {
            viewModel.setPassword(input)
            popBack()
        } else {
            Toast.makeText(context, "새로운 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}
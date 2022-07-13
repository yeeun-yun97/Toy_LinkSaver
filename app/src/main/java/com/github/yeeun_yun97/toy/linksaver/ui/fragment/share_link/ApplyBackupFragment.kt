package com.github.yeeun_yun97.toy.linksaver.ui.fragment.share_link

import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentApplyBackupBinding
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ApplyBackupFragment @Inject constructor(): SjBasicFragment<FragmentApplyBackupBinding>() {


    override fun layoutId(): Int = R.layout.fragment_apply_backup

    override fun onCreateView() {

    }
}
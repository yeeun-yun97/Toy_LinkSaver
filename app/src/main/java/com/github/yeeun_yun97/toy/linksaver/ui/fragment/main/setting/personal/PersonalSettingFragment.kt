package com.github.yeeun_yun97.toy.linksaver.ui.fragment.main.setting.personal

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.SettingItemValue
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentPersonalSettingBinding
import com.github.yeeun_yun97.toy.linksaver.ui.adapter.recycler.SettingListAdapter
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.SettingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PersonalSettingFragment @Inject constructor() : SjBasicFragment<FragmentPersonalSettingBinding>() {
    private val viewModel: SettingViewModel by activityViewModels()
    override fun layoutId(): Int = R.layout.fragment_personal_setting

    override fun onCreateView() {
        binding.settingRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = SettingListAdapter()
        binding.settingRecyclerView.adapter = adapter
        adapter.setList(getSettingList())

        lifecycleScope.launch(Dispatchers.IO) {
            val settingValue = async { viewModel.privateFlow.first() }
            launch(Dispatchers.Main) {
                binding.hidePrivateSwitch.isChecked = settingValue.await()
            }
        }

        binding.hidePrivateSwitch.setOnCheckedChangeListener { compoundButton, isChecked ->
            viewModel.setPrivateMode(isChecked)
        }
    }

    private fun getSettingList(): List<SettingItemValue> {
        return mutableListOf(
            SettingItemValue("비밀번호 설정", ::moveToEditPassword),
        )
    }

    private fun moveToEditPassword() {
        moveToOtherFragment(ChangePasswordFragment())
    }
}
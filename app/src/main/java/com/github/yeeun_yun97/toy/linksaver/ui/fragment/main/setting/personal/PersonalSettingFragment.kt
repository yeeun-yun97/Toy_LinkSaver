package com.github.yeeun_yun97.toy.linksaver.ui.fragment.main.setting.personal

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.SettingItemValue
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentPersonalSettingBinding
import com.github.yeeun_yun97.toy.linksaver.ui.adapter.recycler.SettingListAdapter
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.share_link.ListShareFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.setting.SettingViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PersonalSettingFragment @Inject constructor() : SjBasicFragment<FragmentPersonalSettingBinding>() {
    private val viewModel: SettingViewModel by activityViewModels()

    @Inject lateinit var listShareFragment : ListShareFragment

    override fun layoutId(): Int = R.layout.fragment_personal_setting

    override fun onCreateView() {
        binding.settingRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = SettingListAdapter()
        binding.settingRecyclerView.adapter = adapter
        adapter.setList(getSettingList())

        viewModel.isPrivateMode.observe(viewLifecycleOwner){
            binding.hidePrivateSwitch.isChecked = it
        }

        binding.hidePrivateSwitch.setOnCheckedChangeListener { compoundButton, isChecked ->
            viewModel.setPrivateMode(isChecked)
        }
    }

    private fun getSettingList(): List<SettingItemValue> {
        return mutableListOf(
            SettingItemValue("비밀번호 설정", ::moveToEditPassword),
            SettingItemValue("데이터 및 공유", ::moveToListShare),
        )
    }

    private fun moveToListShare(){
        moveToOtherFragment(listShareFragment)
    }

    private fun moveToEditPassword() {
        moveToOtherFragment(ChangePasswordFragment())
    }
}
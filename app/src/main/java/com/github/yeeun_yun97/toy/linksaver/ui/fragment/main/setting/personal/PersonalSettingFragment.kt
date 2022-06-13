package com.github.yeeun_yun97.toy.linksaver.ui.fragment.main.setting.personal

import androidx.recyclerview.widget.LinearLayoutManager
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.SettingItemValue
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentPersonalSettingBinding
import com.github.yeeun_yun97.toy.linksaver.ui.adapter.recycler.SettingListAdapter
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment

class PersonalSettingFragment : SjBasicFragment<FragmentPersonalSettingBinding>() {
    override fun layoutId(): Int = R.layout.fragment_personal_setting

    override fun onCreateView() {
        binding.settingRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = SettingListAdapter()
        binding.settingRecyclerView.adapter = adapter
        adapter.setList(getSettingList())

        binding.hidePrivateSwitch.setOnCheckedChangeListener { compoundButton, isChecked -> }
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
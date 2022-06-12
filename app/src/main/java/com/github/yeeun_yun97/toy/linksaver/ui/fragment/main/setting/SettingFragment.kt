package com.github.yeeun_yun97.toy.linksaver.ui.fragment.main.setting

import android.content.Intent
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.SettingItemValue
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentSettingBinding
import com.github.yeeun_yun97.toy.linksaver.ui.activity.LockActivity
import com.github.yeeun_yun97.toy.linksaver.ui.adapter.recycler.SettingListAdapter
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.main.setting.app_info.AppInfoFragment
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.main.setting.domain.ListDomainFragment
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.main.setting.tag.ListGroupFragment

class SettingFragment : SjBasicFragment<FragmentSettingBinding>() {
    private val groupFragment = ListGroupFragment()
    private val domainFragment = ListDomainFragment()
    private val appInfoFragment = AppInfoFragment()

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun layoutId(): Int = R.layout.fragment_setting

    override fun onCreateView() {
        binding.settingRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = SettingListAdapter()
        binding.settingRecyclerView.adapter = adapter
        adapter.setList(getSettingList())

        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_SUCCESS) {
                    Toast.makeText(context, "비밀번호 맞음", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "비밀번호 틀림", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun getSettingList(): List<SettingItemValue> {
        return mutableListOf(
            SettingItemValue("사용자 설정", ::moveToPersonalSetting),
            SettingItemValue("도메인 목록", ::moveToViewDomains),
            SettingItemValue("태그 그룹 목록", ::moveToViewTagGroups),
            SettingItemValue("플레이리스트", ::moveToViewPlayLists),
            SettingItemValue("앱 정보 보기", ::moveToViewData),
        )
    }

    private fun moveToPersonalSetting() {
        val intent = Intent(activity, LockActivity::class.java)
        activityResultLauncher.launch(intent)
    }

    private fun moveToViewDomains() {
        this.moveToOtherFragment(domainFragment)
    }

    private fun moveToViewTagGroups() {
        this.moveToOtherFragment(groupFragment)
    }

    private fun moveToViewData() {
        this.moveToOtherFragment(appInfoFragment)
    }

    private fun moveToViewPlayLists() {
        Toast.makeText(requireContext(), "이후 업데이트에서 추가될 예정입니다.", Toast.LENGTH_LONG).show()
    }
}
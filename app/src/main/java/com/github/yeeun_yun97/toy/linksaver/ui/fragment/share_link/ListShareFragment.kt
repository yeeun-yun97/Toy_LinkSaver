package com.github.yeeun_yun97.toy.linksaver.ui.fragment.share_link

import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.SettingItemValue
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentListShareBinding
import com.github.yeeun_yun97.toy.linksaver.ui.adapter.recycler.SettingListAdapter
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.main.setting.tag.ListGroupFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListShareFragment @Inject constructor() : SjBasicFragment<FragmentListShareBinding>() {

    @Inject
    lateinit var saveBackUpFragment: SaveBackupFragment


    override fun layoutId(): Int = R.layout.fragment_list_share

    override fun onCreateView() {
        binding.shareRecyclerView.layoutManager= LinearLayoutManager(context)
        val adapter = SettingListAdapter()
        binding.shareRecyclerView.adapter = adapter
        adapter.setList(getSharingList())
    }

    private fun getSharingList(): List<SettingItemValue> {
        return mutableListOf(
            SettingItemValue("백업 저장하기", ::moveToSaveBackup),
            SettingItemValue("백업 불러오기", ::notYet)
        )
    }

    private fun moveToSaveBackup(){
        this.moveToOtherFragment(saveBackUpFragment)
    }

    private fun notYet() {
        Toast.makeText(requireContext(), "이후 업데이트에서 추가될 예정입니다.", Toast.LENGTH_LONG).show()
    }
}
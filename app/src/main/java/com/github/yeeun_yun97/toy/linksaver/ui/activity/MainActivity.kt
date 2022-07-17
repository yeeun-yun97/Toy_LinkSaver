package com.github.yeeun_yun97.toy.linksaver.ui.activity

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.github.yeeun_yun97.clone.ynmodule.ui.activity.YnMainBaseActivity
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.databinding.ActivityMainBinding
import com.github.yeeun_yun97.toy.linksaver.ui.activity.basic.SjBasicActivity
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.main.playlist.ListVideoFragment
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.main.search.ListLinkFragment
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.main.setting.SettingFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : YnMainBaseActivity<ActivityMainBinding>() {

    // 바텀 내비에 따라 부착할 fragment들.
    @Inject lateinit var linkFragment: ListLinkFragment
    @Inject lateinit var videoFragment: ListVideoFragment
    @Inject lateinit var settingFragment: SettingFragment

    // bottom navigation selection id
    private var selectedItemId = 0

    override fun viewBindingInflate(inflater: LayoutInflater): ActivityMainBinding = ActivityMainBinding.inflate(inflater)

    override fun fragmentContainer(): Int = R.id.fragmentContainer

    override fun getBottomNavi(): BottomNavigationView = binding.bottomNavigation

    override fun homeFragment(): Fragment {
        // 최초 부착할 프래그먼트와 바텀내비의 선택된 메뉴를 동기화.
        this.selectedItemId = R.id.linkItem
        binding.bottomNavigation.selectedItemId = selectedItemId
        return linkFragment
    }

    override fun getBottomNaviHandlerMap(): Map<Int, () -> Unit> {
        return mapOf<Int, () -> Unit>(
            R.id.linkItem to ::moveToLinkFragment,
            R.id.videoItem to ::moveToVideoFragment,
            R.id.settingItem to ::moveToSettingFragment,
        )
    }

    private fun moveToLinkFragment() = this.replaceFragmentTo(linkFragment)
    private fun moveToVideoFragment() = this.replaceFragmentTo(videoFragment)
    private fun moveToSettingFragment() = this.replaceFragmentTo(settingFragment)


}
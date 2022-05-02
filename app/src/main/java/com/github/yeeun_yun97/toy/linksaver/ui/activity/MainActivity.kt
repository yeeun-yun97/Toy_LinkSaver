package com.github.yeeun_yun97.toy.linksaver.ui.activity

import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.databinding.ActivityMainBinding
import com.github.yeeun_yun97.toy.linksaver.ui.activity.basic.ViewBindingBasicActivity
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.ViewLinkFragment
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.ViewTagFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.ReadLinkViewModel

class MainActivity : ViewBindingBasicActivity<ActivityMainBinding>() {
    val viewModel: ReadLinkViewModel by viewModels()
    private val homeFragment = ViewLinkFragment()
    private val tagFragment = ViewTagFragment()

    override fun viewBindingInflate(inflater: LayoutInflater): ActivityMainBinding =
        ActivityMainBinding.inflate(inflater)

    override fun homeFragment(): Fragment = homeFragment

    override fun onCreate() {
        binding.bottomNavigation.selectedItemId=R.id.linkItem
        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.linkItem-> {
                    this.replaceFragmentTo(homeFragment)
                    true
                }
                R.id.domainItem-> {
                    true
                }
                R.id.tagItem-> {
                    this.replaceFragmentTo(tagFragment)
                    true
                }
                R.id.settingItem-> {
                    true
                }
                else -> false
            }
        }



    }
}
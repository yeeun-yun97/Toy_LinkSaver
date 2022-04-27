package com.github.yeeun_yun97.toy.linksaver.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.github.yeeun_yun97.toy.linksaver.databinding.ActivityViewLinkBinding
import com.github.yeeun_yun97.toy.linksaver.ui.activity.basic.ViewBindingBasicActivity
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.EmptyFragment
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.ViewLinkFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.ReadLinkViewModel

class ViewLinkActivity : ViewBindingBasicActivity<ActivityViewLinkBinding>() {
    val viewModel : ReadLinkViewModel by viewModels()
    private val homeFragment = ViewLinkFragment()
    private val emptyFragment = EmptyFragment()

    override fun viewBindingInflate(inflater: LayoutInflater): ActivityViewLinkBinding
    = ActivityViewLinkBinding.inflate(inflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.floatingActionView.setOnClickListener { startEditActivity() }
        viewModel.linkList.observe(this,{
            if(it.isEmpty()){
                setHomeFragment(emptyFragment)
            }else{
                setHomeFragment(homeFragment)
            }
        })

        /*binding.searchEditText.setOnKeyListener(object:View.OnKeyListener{
            override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {
               viewModel.searchLinkByLinkName(binding.searchEditText.text.toString())
                return false
            }

        })

         */
    }

    private fun startEditActivity() {
        val intent = Intent(this, EditLinkActivity::class.java)
        startActivity(intent)
    }

    override fun homeFragment(): Fragment = homeFragment

}
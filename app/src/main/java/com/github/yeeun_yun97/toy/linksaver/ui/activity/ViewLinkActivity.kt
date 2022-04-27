package com.github.yeeun_yun97.toy.linksaver.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.core.text.trimmedLength
import androidx.fragment.app.Fragment
import com.github.yeeun_yun97.toy.linksaver.databinding.ActivityViewLinkBinding
import com.github.yeeun_yun97.toy.linksaver.ui.activity.basic.ViewBindingBasicActivity
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.EmptyFragment
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.ViewLinkFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.ListMode
import com.github.yeeun_yun97.toy.linksaver.viewmodel.ReadLinkViewModel

class ViewLinkActivity : ViewBindingBasicActivity<ActivityViewLinkBinding>() {
    val viewModel: ReadLinkViewModel by viewModels()
    private val homeFragment = ViewLinkFragment()
    private val emptyFragment = EmptyFragment()

    override fun viewBindingInflate(inflater: LayoutInflater): ActivityViewLinkBinding =
        ActivityViewLinkBinding.inflate(inflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.floatingActionView.setOnClickListener { startEditActivity() }
        viewModel.linkList.observe(this, {
            if (viewModel.mode == ListMode.MODE_ALL) {
                if (it.isEmpty()) {
                    setHomeFragment(emptyFragment)
                } else {
                    setHomeFragment(homeFragment)
                }
            }
        })

        viewModel.searchLinkList.observe(this, {
            if (viewModel.mode == ListMode.MODE_SEARCH) {
                if (it.isEmpty()) {
                    setHomeFragment(emptyFragment)
                } else {
                    setHomeFragment(homeFragment)
                }
            }
        })

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                end: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.searchLinkByLinkName(binding.searchEditText.text.toString())
                Log.d("typed", binding.searchEditText.text.toString())
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

    }

    private fun startEditActivity() {
        val intent = Intent(this, EditLinkActivity::class.java)
        startActivity(intent)
    }

    override fun homeFragment(): Fragment = homeFragment
}
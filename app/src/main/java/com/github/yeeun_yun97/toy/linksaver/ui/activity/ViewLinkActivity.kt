package com.github.yeeun_yun97.toy.linksaver.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.github.yeeun_yun97.toy.linksaver.databinding.ActivityViewLinkBinding
import com.github.yeeun_yun97.toy.linksaver.ui.activity.basic.ViewBindingBasicActivity
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.ViewLinkFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.ReadLinkViewModel

class ViewLinkActivity : ViewBindingBasicActivity<ActivityViewLinkBinding>() {
    val viewModel: ReadLinkViewModel by viewModels()
    private val homeFragment = ViewLinkFragment()

    override fun viewBindingInflate(inflater: LayoutInflater): ActivityViewLinkBinding =
        ActivityViewLinkBinding.inflate(inflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
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
                     binding.searchEditText.setOnClickListener {
                         moveToSearchFragment()
                     }

                     */

    }


    override fun homeFragment(): Fragment = homeFragment
}
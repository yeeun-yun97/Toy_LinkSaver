package com.github.yeeun_yun97.toy.linksaver.ui.fragment

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentSearchBinding
import com.github.yeeun_yun97.toy.linksaver.ui.component.SjTagChip
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.DataBindingBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.ReadLinkViewModel
import com.github.yeeun_yun97.toy.linksaver.viewmodel.SearchViewModel

class SearchFragment : DataBindingBasicFragment<FragmentSearchBinding>() {

    val viewModel: SearchViewModel by activityViewModels()
    val readLinkViewModel:ReadLinkViewModel by activityViewModels()

    override fun layoutId(): Int = R.layout.fragment_search

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.searchEditText.requestFocus()

        viewModel.tagList.observe(viewLifecycleOwner, Observer {
            binding.tagChipGroup.removeAllViews()
            for (tag in it) {
                val tag = SjTagChip(requireContext(), tag)
                binding.tagChipGroup.addView(tag)
            }
        })

        binding.searchEditText.setOnEditorActionListener(object:TextView.OnEditorActionListener{
            override fun onEditorAction(TextView: TextView?, actionId: Int, keyEvent: KeyEvent?): Boolean {
                if(actionId== EditorInfo.IME_ACTION_SEARCH){
                    readLinkViewModel.searchLinkByLinkName(binding.searchEditText.text.toString())
                    popBack()
                }
                return false
            }
        })


        return binding.root
    }
}




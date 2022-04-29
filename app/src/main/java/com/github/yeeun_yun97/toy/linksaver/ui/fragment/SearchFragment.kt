package com.github.yeeun_yun97.toy.linksaver.ui.fragment

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.CompoundButton
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.SjSearch
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentSearchBinding
import com.github.yeeun_yun97.toy.linksaver.ui.adapter.SearchesAdapter
import com.github.yeeun_yun97.toy.linksaver.ui.component.SjTagChip
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.DataBindingBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.ReadLinkViewModel
import com.github.yeeun_yun97.toy.linksaver.viewmodel.SearchViewModel

class SearchFragment : DataBindingBasicFragment<FragmentSearchBinding>() {

    val viewModel: SearchViewModel by activityViewModels()
    val readLinkViewModel: ReadLinkViewModel by activityViewModels()

    override fun layoutId(): Int = R.layout.fragment_search

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.searchEditText.requestFocus()

        viewModel.tagList.observe(viewLifecycleOwner, Observer {
            val onCheckedListener = object : CompoundButton.OnCheckedChangeListener {
                override fun onCheckedChanged(btn: CompoundButton?, isChecked: Boolean) {
                    val chip = btn as SjTagChip
                    if (isChecked) {
                        viewModel.selectedTags.add(chip.tag)
                    } else {
                        viewModel.selectedTags.remove(chip.tag)
                    }
                }
            }
            binding.tagChipGroup.removeAllViews()
            for (tag in it) {
                val tag = SjTagChip(requireContext(), tag)
                tag.setOnCheckedChangeListener(onCheckedListener)
                binding.tagChipGroup.addView(tag)
            }
        })

        binding.searchEditText.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(
                TextView: TextView?,
                actionId: Int,
                keyEvent: KeyEvent?
            ): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search(binding.searchEditText.text.toString())
                }
                return false
            }
        })



        binding.recentSearchedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = SearchesAdapter(::setSearch)
        binding.recentSearchedRecyclerView.adapter = adapter
        viewModel.searchList.observe(viewLifecycleOwner,
            {
                adapter.itemList = it
                adapter.notifyDataSetChanged()
            }
        )


        binding.deleteImageView.setOnClickListener { deleteAllSearch() }
        binding.deleteTextView.setOnClickListener { deleteAllSearch() }
        return binding.root
    }

    private fun deleteAllSearch(){
        viewModel.deleteAllSearch()
    }

    private fun setSearch(keyword: String, tags: List<SjTag>) {
        viewModel.selectedTags.clear()
        binding.tagChipGroup.clearCheck()
        viewModel.selectedTags.addAll(tags)
        this.search(keyword)
    }

    private fun search(keyword: String) {
        readLinkViewModel.searchLinkByLinkName(keyword)
        viewModel.saveSearch(SjSearch(keyword = keyword))
        popBack()
    }

}




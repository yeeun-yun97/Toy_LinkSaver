package com.github.yeeun_yun97.toy.linksaver.ui.fragment

import android.view.inputmethod.EditorInfo
import android.widget.CompoundButton
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentSearchBinding
import com.github.yeeun_yun97.toy.linksaver.ui.adapter.RecyclerSearchAdapter
import com.github.yeeun_yun97.toy.linksaver.ui.component.SjTagChip
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.ReadLinkViewModel

class SearchFragment : SjBasicFragment<FragmentSearchBinding>() {
    val viewModel: ReadLinkViewModel by activityViewModels()

    // override methods
    override fun layoutId(): Int = R.layout.fragment_search

    override fun onCreateView() {
        //set binding
        binding.viewModel = viewModel

        // set focus
        binding.searchEditText.requestFocus()

        // chip check listener
        val onCheckedListener =
            CompoundButton.OnCheckedChangeListener { btn, isChecked ->
                val chip = btn as SjTagChip
                if (isChecked) {
                    viewModel.selectedTags.add(chip.tag)
                } else {
                    viewModel.selectedTags.remove(chip.tag)
                }
            }

        // set tag list
        viewModel.tagList.observe(viewLifecycleOwner, {
            binding.tagChipGroup.removeAllViews()
            for (tag in it) {
                val chip = SjTagChip(requireContext(), tag)
                chip.setOnCheckedChangeListener(onCheckedListener)
                binding.tagChipGroup.addView(chip)
            }
        })

        // user input enter(action search) -> search start.
        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchAndPopBack()
            }
            false
        }

        // click search Icon -> search start.
        binding.searchImageView.setOnClickListener {
            searchAndPopBack()
        }

        // set recyclerview search set
        binding.recentSearchedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = RecyclerSearchAdapter(::setSearch)
        binding.recentSearchedRecyclerView.adapter = adapter
        viewModel.searchList.observe(viewLifecycleOwner,
            {
                adapter.setList(it)
            }
        )

        // handle user click event
        binding.deleteImageView.setOnClickListener { deleteAllSearchSet() }
        binding.deleteTextView.setOnClickListener { deleteAllSearchSet() }
    }

    //handle user click methods
    private fun deleteAllSearchSet() {
        viewModel.deleteAllSearch()
    }

    private fun setSearch(keyword: String, tags: List<SjTag>) {
        viewModel.searchWord.postValue(keyword)
        viewModel.selectedTags.clear()
        viewModel.selectedTags.addAll(tags)
        this.searchAndPopBack()
    }

    private fun searchAndPopBack() {
        viewModel.searchLinkBySearchSet()
        popBack()
    }

}




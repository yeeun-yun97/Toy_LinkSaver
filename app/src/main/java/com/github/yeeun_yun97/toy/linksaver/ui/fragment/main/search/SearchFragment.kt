package com.github.yeeun_yun97.toy.linksaver.ui.fragment.main.search

import android.graphics.Rect
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.CompoundButton
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTagGroupWithTags
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentSearchBinding
import com.github.yeeun_yun97.toy.linksaver.ui.adapter.recycler.SearchSetAdapter
import com.github.yeeun_yun97.toy.linksaver.ui.component.SjTagChip
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.search.SearchLinkViewModel

class SearchFragment : SjBasicFragment<FragmentSearchBinding>() {
    val viewModel: SearchLinkViewModel by activityViewModels()

    // drawable resources
    private val deleteIcon by lazy {
        AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.ic_baseline_close_24
        )
    }
    private val searchIcon by lazy {
        AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.ic_baseline_search_24
        )
    }


    // override methods
    override fun layoutId(): Int = R.layout.fragment_search

    override fun onCreateView() {
        //set binding
        binding.viewModel = viewModel

        // set auto focus on Search Field
        binding.searchEditText.requestFocus()

        // hide empty views
        binding.emptySearchSetGroup.visibility = View.GONE
        binding.emptyTagGroup.visibility = View.GONE

        // set tag list
        viewModel.tagGroups.observe(viewLifecycleOwner, {
            if (viewModel.tagDefaultGroup.value != null)
                setTagList(viewModel.tagDefaultGroup.value!!, it)
        })
        viewModel.tagDefaultGroup.observe(viewLifecycleOwner, {
            if (viewModel.tagGroups.value != null)
                setTagList(it, viewModel.tagGroups.value!!)
        })
        viewModel.bindingTargetTags.observe(viewLifecycleOwner, {
            if (viewModel.tagGroups.value != null && viewModel.tagDefaultGroup.value != null)
                setTagList(viewModel.tagDefaultGroup.value!!, viewModel.tagGroups.value!!)
        })

        // user input enter(action search) -> search start.
        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchAndPopBack()
            }
            false
        }

        // user input -> set search icon
        viewModel.bindingSearchWord.observe(viewLifecycleOwner, {
            if (it.isNullOrEmpty() && viewModel.isSearchSetEmpty()) {
                binding.searchImageView.setImageDrawable(deleteIcon)
            } else {
                binding.searchImageView.setImageDrawable(searchIcon)
            }
        })

        // click search Icon -> search start.
        binding.searchImageView.setOnClickListener {
            searchAndPopBack()
        }

        // set recyclerview search set
        binding.recentSearchedRecyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val adapter = SearchSetAdapter(::setSearch, ::searchAndPopBack)
        binding.recentSearchedRecyclerView.addItemDecoration(
            object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect.left = 15
                    outRect.right = 15
                    outRect.bottom = 20
                    outRect.top = 20
                }
            }
        )
        binding.recentSearchedRecyclerView.adapter = adapter
        viewModel.searchList.observe(viewLifecycleOwner,
            {
                if (it.isNullOrEmpty()) {
                    binding.emptySearchSetGroup.visibility = View.VISIBLE
                } else {
                    binding.emptySearchSetGroup.visibility = View.GONE
                }
                adapter.setList(it)
            }
        )

        // handle user click event
        val onClickListener = View.OnClickListener { deleteAllSearchSet() }
        binding.deleteImageView.setOnClickListener(onClickListener)
        binding.deleteTextView.setOnClickListener(onClickListener)
    }

    private fun setTagList(
        defaultGroup: SjTagGroupWithTags,
        groups: List<SjTagGroupWithTags>
    ) {
        if (groups.isNullOrEmpty() && defaultGroup.tags.isEmpty()) {
            binding.emptyTagGroup.visibility = View.VISIBLE
        } else {
            binding.emptyTagGroup.visibility = View.GONE
        }
        val onCheckedListener =
            CompoundButton.OnCheckedChangeListener { btn, isChecked ->
                val chip = btn as SjTagChip
                if (isChecked) {
                    viewModel.addTag(chip.tag)
                    binding.searchImageView.setImageDrawable(searchIcon)
                } else {
                    viewModel.removeTag(chip.tag)
                    if (viewModel.isSearchSetEmpty()) {
                        binding.searchImageView.setImageDrawable(deleteIcon)
                    }
                }
            }

        binding.tagChipGroup.removeAllViews()
        for (def in defaultGroup.tags) {
            val chip = SjTagChip(requireContext(), def)
            chip.isChecked = viewModel.containsTag(def)
            chip.setOnCheckedChangeListener(onCheckedListener)
            binding.tagChipGroup.addView(chip)
        }
        for (group in groups) {
            for (tag in group.tags) {
                val chip = SjTagChip(requireContext(), tag)
                chip.isChecked = viewModel.containsTag(tag)
                chip.setOnCheckedChangeListener(onCheckedListener)
                chip.setText("${group.tagGroup.name}: ${tag.name}")
                binding.tagChipGroup.addView(chip)
            }
        }
    }


    // search methods
    private fun setSearch(keyword: String, tags: List<SjTag>) {
        viewModel.bindingSearchWord.postValue(keyword)
        viewModel.setTags(tags)
    }

    private fun searchAndPopBack() {
        viewModel.searchLinkBySearchSetAndSave()
        popBack()
    }

    //handle user click methods
    private fun deleteAllSearchSet() {
        viewModel.deleteAllSearch()
    }

}






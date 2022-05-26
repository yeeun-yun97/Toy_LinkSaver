package com.github.yeeun_yun97.toy.linksaver.ui.fragment

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
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentSearchBinding
import com.github.yeeun_yun97.toy.linksaver.ui.adapter.RecyclerSearchGalleryAdapter
import com.github.yeeun_yun97.toy.linksaver.ui.component.SjTagChip
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.ReadLinkViewModel

class SearchFragment : SjBasicFragment<FragmentSearchBinding>() {
    val viewModel: ReadLinkViewModel by activityViewModels()

    private val deleteIcon by lazy {
        AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_close_24)
    }
    private val searchIcon by lazy {
        AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_search_24)
    }


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
                    binding.searchImageView.setImageDrawable(searchIcon)

                } else {
                    viewModel.selectedTags.remove(chip.tag)
                    if (viewModel.isSearchSetEmpty()) {
                        binding.searchImageView.setImageDrawable(deleteIcon)
                    }
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

        // user input -> set search icon
        viewModel.searchWord.observe(viewLifecycleOwner, {
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
//        binding.recentSearchedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recentSearchedRecyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

//        val adapter = RecyclerSearchAdapter(::setSearch)
        val adapter = RecyclerSearchGalleryAdapter(::setSearch)
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
        viewModel.searchLinkBySearchSetAndSave()
        popBack()
    }

}






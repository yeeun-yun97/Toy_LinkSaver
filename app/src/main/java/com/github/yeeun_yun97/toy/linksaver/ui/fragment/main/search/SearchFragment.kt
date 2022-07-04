package com.github.yeeun_yun97.toy.linksaver.ui.fragment.main.search

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.CompoundButton
import androidx.activity.OnBackPressedCallback
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
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjUsePrivateModeFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.search.ListLinkBySearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment @Inject constructor() : SjUsePrivateModeFragment<FragmentSearchBinding>() {
    private val searchViewModel: ListLinkBySearchViewModel by activityViewModels()

    // drawable resources
    private val deleteIcon by lazy {
        AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.ic_button_cancel_search
        )
    }
    private val searchIcon by lazy {
        AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.ic_button_search_start
        )
    }

    private val onCheckedListener =
        CompoundButton.OnCheckedChangeListener { btn, isChecked ->
            val chip = btn as SjTagChip
            if (isChecked) {
                searchViewModel.addTag(chip.tag)
                binding.searchImageView.setImageDrawable(searchIcon)
            } else {
                searchViewModel.removeTag(chip.tag)
                if (searchViewModel.isSearchSetEmpty()) {
                    binding.searchImageView.setImageDrawable(deleteIcon)
                }
            }
        }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            searchViewModel.startSearchAndSaveIfNotEmpty()
            popBack()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onDetach() {
        super.onDetach()
        onBackPressedCallback.remove()
    }

    override fun layoutId(): Int = R.layout.fragment_search

    override fun onResume() {
        super.onResume()
        //searchViewModel.isPrivateMode = settingViewModel.isPrivateMode.value!!
        searchViewModel.refreshData()
    }

    override fun onCreateView() {
        //set binding
        binding.viewModel = searchViewModel

        applyPrivateToViewModel(searchViewModel)

        // set auto focus on Search Field
        binding.searchEditText.requestFocus()

        // hide empty views
        binding.emptySearchSetGroup.visibility = View.GONE
        binding.emptyTagGroup.visibility = View.GONE

        // set recyclerview search set
        initRecyclerView()

        searchViewModel.tagGroups.observe(viewLifecycleOwner) {
            setTagList(searchViewModel.defaultTags.value, it)
        }
        searchViewModel.defaultTags.observe(viewLifecycleOwner) {
            setTagList(it, searchViewModel.tagGroups.value)
        }
        searchViewModel.bindingSearchTags.observe(viewLifecycleOwner) {
            setTagList(searchViewModel.defaultTags.value, searchViewModel.tagGroups.value)
        }

        // user input enter(action search) -> search start.
        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchAndPopBack()
            }
            false
        }

        // user input -> set search icon
        searchViewModel.bindingSearchWord.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty() && searchViewModel.isSearchSetEmpty()) {
                binding.searchImageView.setImageDrawable(deleteIcon)
            } else {
                binding.searchImageView.setImageDrawable(searchIcon)
            }
        }

        setOnClickListeners()
    }


    override fun initRecyclerView() {
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
        searchViewModel.bindingSearchSets.observe(viewLifecycleOwner) {
            adapter.setList(it)
        }
    }

    private fun setTagList(
        defaultGroup: SjTagGroupWithTags?,
        groups: List<SjTagGroupWithTags>?
    ) {
        if (groups.isNullOrEmpty() && defaultGroup != null) {
            binding.emptyTagGroup.visibility = View.VISIBLE
        } else {
            binding.emptyTagGroup.visibility = View.GONE
        }

        setTagsToChipGroupChildren(
            defaultGroup,
            groups,
            ::isTagSelected,
            binding.tagChipGroup,
            onCheckedListener
        )
    }

    private fun isTagSelected(tag: SjTag) = searchViewModel.containsTag(tag)


    // handle user click methods
    override fun setOnClickListeners() {
        val onClickListener = View.OnClickListener { deleteAllSearchSet() }
        binding.deleteImageView.setOnClickListener(onClickListener)
        binding.deleteTextView.setOnClickListener(onClickListener)

        // click search Icon -> search start.
        binding.searchImageView.setOnClickListener { searchAndPopBack() }
    }

    private fun setSearch(keyword: String, tags: List<SjTag>) =
        searchViewModel.setSearch(keyword, tags)

    private fun deleteAllSearchSet() =
        searchViewModel.deleteAllSearchSet()

    private fun searchAndPopBack() {
        searchViewModel.startSearchAndSaveIfNotEmpty()
        popBack()
    }


}






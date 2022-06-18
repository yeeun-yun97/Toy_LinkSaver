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
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentSearchBinding
import com.github.yeeun_yun97.toy.linksaver.ui.adapter.recycler.SearchSetAdapter
import com.github.yeeun_yun97.toy.linksaver.ui.component.SjTagChip
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.search.SearchLinkViewModel

class SearchFragment : SjBasicFragment<FragmentSearchBinding>() {
    private val viewModel: SearchLinkViewModel by activityViewModels()
    private lateinit var adapter: SearchSetAdapter

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
                viewModel.addTag(chip.tag)
                binding.searchImageView.setImageDrawable(searchIcon)
            } else {
                viewModel.removeTag(chip.tag)
                if (viewModel.isSearchSetEmpty()) {
                    binding.searchImageView.setImageDrawable(deleteIcon)
                }
            }
        }


//    private fun selectLiveDataBySettingValue(isPrivateMode: Boolean): LiveData<List<SjTagGroupWithTags>> {
//        return if (isPrivateMode) viewModel.publicTagGroups
//        else viewModel.tagGroups
//    }


    // override methods
    override fun layoutId(): Int = R.layout.fragment_search

    override fun onResume() {
        super.onResume()
        viewModel.refreshData()
    }

    override fun onCreateView() {
        //set binding
        binding.viewModel = viewModel

        // set auto focus on Search Field
        binding.searchEditText.requestFocus()

        // hide empty views
        binding.emptySearchSetGroup.visibility = View.GONE
        binding.emptyTagGroup.visibility = View.GONE

        // set recyclerview search set
        initRecyclerView()

/*
lifecycleScope.launch {
            val isPrivateModeDeffer = async(Dispatchers.IO) { settingViewModel.privateFlow.first() }
            val isPrivateMode = isPrivateModeDeffer.await()
            viewModel.isPrivateMode = isPrivateMode

            // set tag list
            val tagGroupsLiveData =
                selectLiveDataBySettingValue(isPrivateMode)
            tagGroupsLiveData.observe(viewLifecycleOwner, {
                if (viewModel.tagDefaultGroup.value != null)
                    setTagList(viewModel.tagDefaultGroup.value!!, it)
            })
            viewModel.tagDefaultGroup.observe(viewLifecycleOwner, {
                if (tagGroupsLiveData.value != null)
                    setTagList(it, tagGroupsLiveData.value!!)
            })
            viewModel.bindingSearchTags.observe(viewLifecycleOwner, {
                if (tagGroupsLiveData.value != null && viewModel.tagDefaultGroup.value != null)
                    setTagList(viewModel.tagDefaultGroup.value!!, tagGroupsLiveData.value!!)
            })

        }

*/

        viewModel.searchSets.observe(viewLifecycleOwner,
            {
                if (it.isNullOrEmpty()) {
                    binding.emptySearchSetGroup.visibility = View.VISIBLE
                } else {
                    binding.emptySearchSetGroup.visibility = View.GONE
                }
                adapter.setList(it)
            }
        )

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

        setOnClickListeners()
    }


    override fun initRecyclerView() {
        binding.recentSearchedRecyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        this.adapter = SearchSetAdapter(::setSearch, ::searchAndPopBack)
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
        binding.recentSearchedRecyclerView.adapter = this.adapter
    }

//    private fun setTagList(
//        defaultGroup: SjTagGroupWithTags,
//        groups: List<SjTagGroupWithTags>
//    ) {
//        if (groups.isNullOrEmpty() && defaultGroup.tags.isEmpty()) {
//            binding.emptyTagGroup.visibility = View.VISIBLE
//        } else {
//            binding.emptyTagGroup.visibility = View.GONE
//        }
//
//        setTagsToChipGroupChildren(
//            defaultGroup,
//            groups,
//            ::isTagSelected,
//            binding.tagChipGroup,
//            onCheckedListener
//        )
//    }
    private fun isTagSelected(tag: SjTag) = viewModel.containsTag(tag)


    // handle user click methods
    override fun setOnClickListeners() {
        val onClickListener = View.OnClickListener { deleteAllSearchSet() }
        binding.deleteImageView.setOnClickListener(onClickListener)
        binding.deleteTextView.setOnClickListener(onClickListener)

        // click search Icon -> search start.
        binding.searchImageView.setOnClickListener { searchAndPopBack() }
    }

    private fun setSearch(keyword: String, tags: List<SjTag>) {
        viewModel.bindingSearchWord.postValue(keyword)
        viewModel.setTags(tags)
    }

    private fun deleteAllSearchSet() {
        viewModel.deleteAllSearchSet()
    }

    private fun searchAndPopBack() {
        viewModel.startSearchAndSaveIfNotEmpty()
        popBack()
    }

}






package com.github.yeeun_yun97.toy.linksaver.ui.fragment.main.search

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.yeeun_yun97.clone.ynmodule.ui.component.DataState
import com.github.yeeun_yun97.clone.ynmodule.ui.component.ViewVisibilityUtil
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLinksAndDomainsWithTags
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentListLinkBinding
import com.github.yeeun_yun97.toy.linksaver.ui.activity.EditLinkActivity
import com.github.yeeun_yun97.toy.linksaver.ui.adapter.recycler.LinkSearchListAdapter
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.main.search.detail_link.DetailLinkFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.SettingViewModel
import com.github.yeeun_yun97.toy.linksaver.viewmodel.search.ListMode
import com.github.yeeun_yun97.toy.linksaver.viewmodel.search.SearchLinkViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first

class ListLinkFragment : SjBasicFragment<FragmentListLinkBinding>() {
    private val viewModel: SearchLinkViewModel by activityViewModels()
    private val settingViewModel: SettingViewModel by viewModels()

    // control view visibility
    private lateinit var viewUtil: ViewVisibilityUtil

    // override methods
    override fun layoutId(): Int = R.layout.fragment_list_link

    override fun onStart() {
        super.onStart()
        Log.d("onStart", "search start, shimmer started")
        if (viewModel.mode == ListMode.MODE_SEARCH) {
            viewUtil.state = DataState.LOADING
        }
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.mode == ListMode.MODE_SEARCH)
            viewModel.searchLinkBySearchSet()
    }

    override fun onCreateView() {
        // set binding variable
        binding.viewModel = viewModel

        // set recycler view
        val adapter = LinkSearchListAdapter(
            detailOperation = ::moveToDetailFragment
        )
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        // set view Util
        viewUtil = ViewVisibilityUtil(
            loadingView = binding.shimmer,
            loadedView = binding.recyclerView,
            emptyView = binding.emptyGroup
        )

        // set list when Mode SEARCH
        val searchLiveData = viewModel.searchLinkList
        searchLiveData.observe(viewLifecycleOwner,
            {
                if (viewModel.mode == ListMode.MODE_SEARCH) {
                    adapter.setList(it)
                    delayAndViewVisibleControl(it)
                }
            }
        )

        lifecycleScope.launch {
            val isPrivateModeDeferred =
                async(Dispatchers.IO) { settingViewModel.privateFlow.first() }
            val isPrivateMode = isPrivateModeDeferred.await()

            // set list when Mode ALL
            val allLiveData = if (isPrivateMode) viewModel.publicLinkList
            else viewModel.linkList
            allLiveData.observe(viewLifecycleOwner,
                {
                    if (viewModel.mode == ListMode.MODE_ALL) {
                        adapter.setList(it)
                        delayAndViewVisibleControl(it)
                    }
                }
            )
        }
        viewModel.bindingTargetTags.observe(viewLifecycleOwner, {
            if (it.isNullOrEmpty()) {
                binding.cancelSearchSetImageView.visibility = View.GONE
            } else {
                binding.cancelSearchSetImageView.visibility = View.VISIBLE
            }
        })

        // handle user click event
        binding.floatingActionView.setOnClickListener { startEditActivity() }
        binding.searchEditText.setOnClickListener {
            this.moveToSearchFragment()
        }
        binding.cancelSearchSetImageView.setOnClickListener {
            viewModel.clearSearchSet()
        }
    }

    private fun delayAndViewVisibleControl(dataList: List<SjLinksAndDomainsWithTags>) {
        CoroutineScope(Dispatchers.Main).launch {
            delay(500)
            if (dataList.isEmpty()) {
                viewUtil.state = DataState.EMPTY
                Log.d("search ended", "empty")
            } else {
                viewUtil.state = DataState.LOADED
                Log.d("search ended", "list")
            }
        }
    }

    private fun moveToSearchFragment() {
        moveToOtherFragment(SearchFragment())
    }

    private fun moveToDetailFragment(lid: Int) {
        moveToOtherFragment(DetailLinkFragment.newInstance(lid))
    }

    private fun startEditActivity() {
        val intent = Intent(requireContext(), EditLinkActivity::class.java)
        startActivity(intent)
    }


}
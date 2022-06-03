package com.github.yeeun_yun97.toy.linksaver.ui.fragment.main.search

import android.content.Intent
import android.util.Log
import androidx.fragment.app.activityViewModels
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
import com.github.yeeun_yun97.toy.linksaver.viewmodel.search.ListMode
import com.github.yeeun_yun97.toy.linksaver.viewmodel.search.SearchLinkViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ListLinkFragment : SjBasicFragment<FragmentListLinkBinding>() {
    private val viewModel: SearchLinkViewModel by activityViewModels()

    // control view visibility
    private lateinit var viewUtil: ViewVisibilityUtil

    // override methods
    override fun layoutId(): Int = R.layout.fragment_list_link

    override fun onStart() {
        super.onStart()
        Log.d("onStart","search start, shimmer started")
        if(viewModel.mode==ListMode.MODE_SEARCH){
            viewUtil.state = DataState.LOADING
            binding.shimmer.startShimmer()
            viewModel.searchLinkBySearchSet()
        }
    }

    override fun onCreateView() {
        // set binding variable
        binding.viewModel=viewModel

        // set recycler view
        val adapter = LinkSearchListAdapter(
            detailOperation = ::moveToDetailFragment
        )
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        // set view Util
        viewUtil = ViewVisibilityUtil(
            loadingView = binding.shimmerRecylerView,
            loadedView = binding.recyclerView,
            emptyView = binding.include.emptyView
        )

        // set list when Mode ALL
        viewModel.linkList.observe(viewLifecycleOwner,
            {
                if (viewModel.mode == ListMode.MODE_ALL) {
                    adapter.setList(it)
                    delayAndViewVisibleControl(it)
                }
            }
        )

        // set list when Mode SEARCH
        viewModel.searchLinkList.observe(viewLifecycleOwner,
            {
                if (viewModel.mode == ListMode.MODE_SEARCH) {
                    adapter.setList(it)
                    delayAndViewVisibleControl(it)
                }
            }
        )

        // handle user click event
        binding.floatingActionView.setOnClickListener { startEditActivity() }
        binding.searchEditText.setOnClickListener {
            this.moveToSearchFragment()
        }
    }

    private fun delayAndViewVisibleControl(dataList: List<SjLinksAndDomainsWithTags>) {
        CoroutineScope(Dispatchers.Main).launch {
            delay(500)
            if (dataList.isEmpty()) {
                viewUtil.state = DataState.EMPTY
                Log.d("search ended","empty")
            } else {
                viewUtil.state = DataState.LOADED
                Log.d("search ended","list")
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
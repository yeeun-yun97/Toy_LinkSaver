package com.github.yeeun_yun97.toy.linksaver.ui.fragment.main.search

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.yeeun_yun97.clone.ynmodule.ui.component.DataState
import com.github.yeeun_yun97.clone.ynmodule.ui.component.ViewVisibilityUtil
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLinksAndDomainsWithTags
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentListLinkBinding
import com.github.yeeun_yun97.toy.linksaver.ui.activity.EditLinkActivity
import com.github.yeeun_yun97.toy.linksaver.ui.adapter.recycler.LinkSearchListAdapter
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.main.search.detail_link.DetailLinkFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.search.SearchLinkViewModel
import kotlinx.coroutines.*


class ListLinkFragment : SjBasicFragment<FragmentListLinkBinding>() {
    private val viewModel: SearchLinkViewModel by activityViewModels()

    // control view visibility
    private lateinit var viewUtil: ViewVisibilityUtil

    // for recyclerView
    private lateinit var adapter: LinkSearchListAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager


    override fun layoutId(): Int = R.layout.fragment_list_link

    override fun onStart() {
        super.onStart()
        viewUtil.state = DataState.LOADING
        viewModel.refreshData()
    }

    override fun onCreateView() {
        // set binding variable
        binding.viewModel = viewModel

        // set recycler view
        initRecyclerView()

        // set view Util
        viewUtil = ViewVisibilityUtil(
            loadingView = binding.shimmer,
            loadedView = binding.recyclerView,
            emptyView = binding.emptyGroup
        )

        // handle user click event
        setOnClickListeners()

        // show or hide Button
        viewModel.bindingSearchTags.observe(viewLifecycleOwner, {
            binding.cancelSearchSetImageView.visibility =
                when (it.isNullOrEmpty()) {
                    true -> View.GONE
                    false -> View.VISIBLE
                }
        })

        // set adapter list
        viewModel.dataList.observe(viewLifecycleOwner, {
            Log.d("똥","데이터 리스트에 ${it.size}건 들어옴!")
            if (it != null) setAdapterList(it)
        })
    }

    private fun setAdapterList(list: List<SjLinksAndDomainsWithTags>) {
        CoroutineScope(Dispatchers.Main).launch {
            adapter.setList(list)
            delay(500)
            if (list.isEmpty()) {
                viewUtil.state = DataState.EMPTY
            } else {
                viewUtil.state = DataState.LOADED
            }
        }
    }

    override fun initRecyclerView() {
        this.adapter = LinkSearchListAdapter(detailOperation = ::moveToDetailFragment)
        this.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = this.adapter
        binding.recyclerView.layoutManager = this.layoutManager
    }


    // functions for user event
    override fun setOnClickListeners() {
        binding.floatingActionView.setOnClickListener { startEditActivity() }
        binding.searchEditText.setOnClickListener { moveToSearchFragment() }
        binding.cancelSearchSetImageView.setOnClickListener { viewModel.clearSearchSet() }
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
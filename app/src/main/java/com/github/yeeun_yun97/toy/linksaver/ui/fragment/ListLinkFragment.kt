package com.github.yeeun_yun97.toy.linksaver.ui.fragment

import android.content.Intent
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLink
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentListLinkBinding
import com.github.yeeun_yun97.toy.linksaver.ui.activity.EditLinkActivity
import com.github.yeeun_yun97.toy.linksaver.ui.adapter.RecyclerSearchLinkAdapter
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.ListMode
import com.github.yeeun_yun97.toy.linksaver.viewmodel.ReadLinkViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ListLinkFragment : SjBasicFragment<FragmentListLinkBinding>() {
    val viewModel: ReadLinkViewModel by activityViewModels()


    // override methods
    override fun layoutId(): Int = R.layout.fragment_list_link

    override fun onResume() {
        super.onResume()
        binding.shimmerRecylerView.visibility=View.VISIBLE
        binding.include.emptyView.visibility = View.GONE
        binding.recyclerView.visibility=View.GONE
        binding.shimmer.startShimmer()
        viewModel.searchLinkBySearchSet()
    }

    override fun onCreateView() {
        // set recycler view
        val adapter = RecyclerSearchLinkAdapter(
            detailOperation = ::moveToDetailFragment
        )
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)


        // set list when Mode ALL
        viewModel.linkList.observe(viewLifecycleOwner,
            {
                CoroutineScope(Dispatchers.Main).launch{
                    delay(1500)
                    binding.shimmerRecylerView.visibility=View.GONE

                    if (it.isEmpty()) {
                        binding.include.emptyView.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.GONE
                    } else {
                        binding.include.emptyView.visibility = View.GONE
                        binding.recyclerView.visibility = View.VISIBLE
                    }
                }
                if (viewModel.mode == ListMode.MODE_ALL) {
                    adapter.setList(it)
                }
            }
        )

        // set list when Mode SEARCH
        viewModel.searchLinkList.observe(viewLifecycleOwner,
            {
                if (viewModel.mode == ListMode.MODE_SEARCH) {
                    adapter.setList(it)
                    if (it.isEmpty()) {
                        binding.include.emptyView.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.GONE
                    } else {
                        binding.include.emptyView.visibility = View.GONE
                        binding.recyclerView.visibility = View.VISIBLE
                    }

                }
            }
        )


        // handle user click event
        binding.floatingActionView.setOnClickListener { startEditActivity() }
        binding.searchEditText.setOnClickListener {
            this.moveToSearchFragment()
        }
    }


    // handle user event methods
    private fun deleteLink(link: SjLink, tags: List<SjTag>) {
        viewModel.deleteLink(link, tags)
        if (viewModel.mode == ListMode.MODE_SEARCH) {
            viewModel.searchLinkBySearchSetAndSave()
        }
    }

    private fun moveToSearchFragment() {
        moveToOtherFragment(SearchFragment())
    }

    private fun moveToDetailFragment(lid:Int) {
        moveToOtherFragment(DetailLinkFragment.newInstance(lid))
    }


    private fun startEditActivity() {
        val intent = Intent(requireContext(), EditLinkActivity::class.java)
        startActivity(intent)
    }


}
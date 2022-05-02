package com.github.yeeun_yun97.toy.linksaver.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLink
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentViewLinkBinding
import com.github.yeeun_yun97.toy.linksaver.ui.activity.EditLinkActivity
import com.github.yeeun_yun97.toy.linksaver.ui.adapter.LinksAdapter
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.DataBindingBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.ListMode
import com.github.yeeun_yun97.toy.linksaver.viewmodel.ReadLinkViewModel

class ViewLinkFragment : DataBindingBasicFragment<FragmentViewLinkBinding>() {
    val viewModel: ReadLinkViewModel by activityViewModels()

    override fun layoutId(): Int = R.layout.fragment_view_link

    override fun onStart() {
        super.onStart()
        Log.i("viewModel mode",viewModel.mode.name)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val adapter = LinksAdapter(::startWebBrowser,::startEditActivityToUpdate, ::deleteLink)
        viewModel.linkList.observe(viewLifecycleOwner,
            {
                if (viewModel.mode == ListMode.MODE_ALL) {
                    adapter.itemList = it
                    adapter.notifyDataSetChanged()
                }
            }
        )

        viewModel.linkList.observe(viewLifecycleOwner, {
            if (viewModel.mode == ListMode.MODE_ALL) {
                if (it.isEmpty()) {
                    binding.include.emptyView.visibility=View.VISIBLE
                    binding.recyclerView.visibility=View.GONE
                } else {
                    binding.include.emptyView.visibility=View.GONE
                    binding.recyclerView.visibility=View.VISIBLE
                }
            }
        })

        viewModel.searchLinkList.observe(viewLifecycleOwner, {
            if (viewModel.mode == ListMode.MODE_SEARCH) {
                if (it.isEmpty()) {
                    binding.include.emptyView.visibility=View.VISIBLE
                    binding.recyclerView.visibility=View.GONE
                } else {
                    binding.include.emptyView.visibility=View.GONE
                    binding.recyclerView.visibility=View.VISIBLE
                }
            }
        })

        binding.floatingActionView.setOnClickListener { startEditActivity() }


        viewModel.searchLinkList.observe(this,
            {
                if (viewModel.mode == ListMode.MODE_SEARCH) {
                    adapter.itemList = it
                    adapter.notifyDataSetChanged()
                }
            }
        )

        binding.searchEditText.setOnClickListener {
            this.moveToSearchFragment()
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    private fun deleteLink(link: SjLink, tags: List<SjTag>) {
        viewModel.deleteLink(link, tags)
    }

    private fun moveToSearchFragment(){
        moveToOtherFragment(SearchFragment())
    }

    private fun startWebBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
    private fun startEditActivity() {
        val intent = Intent(requireContext(), EditLinkActivity::class.java)
        startActivity(intent)
    }
    private fun startEditActivityToUpdate(lid:Int){
        val intent = Intent(requireContext(), EditLinkActivity::class.java)
        intent.putExtra("lid",lid)
        startActivity(intent)
    }

}
package com.github.yeeun_yun97.toy.linksaver.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLink
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentViewLinkBinding
import com.github.yeeun_yun97.toy.linksaver.ui.adapter.LinksAdapter
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.DataBindingBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.ReadLinkViewModel

class ViewLinkFragment : DataBindingBasicFragment<FragmentViewLinkBinding>() {
    val viewModel: ReadLinkViewModel by activityViewModels()

    override fun layoutId(): Int = R.layout.fragment_view_link

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val adapter = LinksAdapter(::startWebBrowser, ::deleteLink)
        viewModel.linkList.observe(this,
            {
                adapter.itemList = it
                adapter.notifyDataSetChanged()
            }
        )

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    private fun deleteLink(link: SjLink, tags: List<SjTag>) {
        viewModel.deleteLink(link, tags)
    }

    private fun editLink() {}

    private fun shareLink() {}

    private fun startWebBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

}
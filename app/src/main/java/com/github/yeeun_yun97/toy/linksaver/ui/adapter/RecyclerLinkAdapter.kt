package com.github.yeeun_yun97.toy.linksaver.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.yeeun_yun97.clone.ynmodule.ui.adapter.RecyclerBasicAdapter
import com.github.yeeun_yun97.clone.ynmodule.ui.adapter.RecyclerBasicViewHolder
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLink
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLinksAndDomainsWithTags
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.databinding.ItemLinksBinding

class RecyclerLinkAdapter(
    private val openOperation: (String) -> Unit,
    private val updateOperation: (Int) -> Unit,
    private val deleteOperation: (SjLink, List<SjTag>) -> Unit,
    private val detailOperation: (Int) -> Unit,
) :
    RecyclerBasicAdapter<SjLinksAndDomainsWithTags, LinksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinksViewHolder {
        val binding = ItemLinksBinding.inflate(LayoutInflater.from(parent.context))
        return LinksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LinksViewHolder, item: SjLinksAndDomainsWithTags) {
        holder.setLink(item,openOperation,updateOperation,deleteOperation,detailOperation)
    }
}

class LinksViewHolder(binding: ItemLinksBinding) :
    RecyclerBasicViewHolder<ItemLinksBinding>(binding) {

    fun setLink(
        link: SjLinksAndDomainsWithTags,
        openOperation: (String) -> Unit,
        updateOperation: (Int) -> Unit,
        deleteOperation: (SjLink, List<SjTag>) -> Unit,
        detailOperation: (Int) -> Unit
    ) {
        binding.link = link
        binding.linksItemWebButton.setOnClickListener { openOperation("${link.domain.url}${link.link.url}") }
        binding.linksItemEditButton.setOnClickListener { updateOperation(link.link.lid) }
        binding.linksItemDeleteButton.setOnClickListener { deleteOperation(link.link, link.tags) }
        binding.root.setOnClickListener { detailOperation(link.link.lid) }
    }

}
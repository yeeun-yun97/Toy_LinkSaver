package com.github.yeeun_yun97.toy.linksaver.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.yeeun_yun97.clone.ynmodule.ui.adapter.RecyclerBasicAdapter
import com.github.yeeun_yun97.clone.ynmodule.ui.adapter.RecyclerBasicViewHolder
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLinksAndDomainsWithTags
import com.github.yeeun_yun97.toy.linksaver.databinding.ItemLinkSearchBinding

class RecyclerSearchLinkAdapter(
    private val detailOperation: (Int) -> Unit,
) :
    RecyclerBasicAdapter<SjLinksAndDomainsWithTags, LinksSearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinksSearchViewHolder {
        val binding = ItemLinkSearchBinding.inflate(LayoutInflater.from(parent.context))
        return LinksSearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LinksSearchViewHolder, item: SjLinksAndDomainsWithTags) {
        holder.setLink(item, detailOperation)
    }
}

class LinksSearchViewHolder(binding: ItemLinkSearchBinding) :
    RecyclerBasicViewHolder<ItemLinkSearchBinding>(binding) {

    fun setLink(
        link: SjLinksAndDomainsWithTags,
        detailOperation: (Int) -> Unit
    ) {
        binding.data = link
        binding.root.setOnClickListener { detailOperation(link.link.lid) }
    }

}
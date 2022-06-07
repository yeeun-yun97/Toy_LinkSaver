package com.github.yeeun_yun97.toy.linksaver.ui.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.github.yeeun_yun97.clone.ynmodule.ui.adapter.RecyclerBasicAdapter
import com.github.yeeun_yun97.clone.ynmodule.ui.adapter.RecyclerBasicViewHolder
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.ELinkType
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLinksAndDomainsWithTags
import com.github.yeeun_yun97.toy.linksaver.databinding.ItemLinkSearchBinding

class LinkSearchListAdapter(
    private val detailOperation: (Int) -> Unit,
) :
    RecyclerBasicAdapter<SjLinksAndDomainsWithTags, LinkSearchListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkSearchListViewHolder {
        val binding = ItemLinkSearchBinding.inflate(LayoutInflater.from(parent.context))
        return LinkSearchListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LinkSearchListViewHolder, item: SjLinksAndDomainsWithTags) {
        holder.setLink(item, detailOperation)
    }
}

class LinkSearchListViewHolder(binding: ItemLinkSearchBinding) :
    RecyclerBasicViewHolder<ItemLinkSearchBinding>(binding) {

    fun setLink(
        link: SjLinksAndDomainsWithTags,
        detailOperation: (Int) -> Unit
    ) {
        binding.data = link
        binding.root.setOnClickListener { detailOperation(link.link.lid) }
        val drawable = when (link.link.type) {
                ELinkType.video -> R.drawable.ic_icons8_video
                ELinkType.link -> R.drawable.ic_icons8_link
            }
        Glide.with(itemView.context).load(drawable).into(binding.linkIconImageView)
    }

}
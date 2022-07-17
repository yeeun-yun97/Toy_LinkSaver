package com.github.yeeun_yun97.toy.linksaver.ui.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.github.yeeun_yun97.clone.ynmodule.ui.adapter.YnBaseAdapter
import com.github.yeeun_yun97.clone.ynmodule.ui.adapter.YnBaseViewHolder
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.ELinkType
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLinksAndDomainsWithTags
import com.github.yeeun_yun97.toy.linksaver.databinding.ItemLinkSearchBinding

class LinkSearchListAdapter(
    private val detailOperation: (Int) -> Unit,
) :
    YnBaseAdapter<SjLinksAndDomainsWithTags, LinkSearchListViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LinkSearchListViewHolder {
        val binding = ItemLinkSearchBinding.inflate(LayoutInflater.from(parent.context))
        return LinkSearchListViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: LinkSearchListViewHolder,
        item: SjLinksAndDomainsWithTags,
        position: Int
    ) {
        holder.setLink(item, detailOperation)
    }
}

class LinkSearchListViewHolder(binding: ItemLinkSearchBinding) :
    YnBaseViewHolder<ItemLinkSearchBinding>(binding) {

    fun setLink(
        link: SjLinksAndDomainsWithTags,
        detailOperation: (Int) -> Unit
    ) {
        binding.data = link
        binding.root.setOnClickListener { detailOperation(link.link.lid) }
        val drawable = when (link.link.type) {
            ELinkType.video -> R.drawable.ic_recycler_type_video
            ELinkType.link -> R.drawable.ic_recycler_type_link
        }
        Glide.with(itemView.context).load(drawable).into(binding.linkIconImageView)
    }

}
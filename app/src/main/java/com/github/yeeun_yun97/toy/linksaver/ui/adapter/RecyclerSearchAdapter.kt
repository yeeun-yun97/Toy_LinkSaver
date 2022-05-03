package com.github.yeeun_yun97.toy.linksaver.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.yeeun_yun97.clone.ynmodule.ui.adapter.RecyclerBasicAdapter
import com.github.yeeun_yun97.clone.ynmodule.ui.adapter.RecyclerBasicViewHolder
import com.github.yeeun_yun97.toy.linksaver.data.model.SjSearchWithTags
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.databinding.ItemSearchSetBinding

class RecyclerSearchAdapter(
    private val clickOperation: (String, List<SjTag>) -> Unit
) : RecyclerBasicAdapter<SjSearchWithTags, SearchesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchesViewHolder {
        val binding =
            ItemSearchSetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchesViewHolder, item: SjSearchWithTags) {
        holder.setSearch(item, clickOperation)
    }

}

class SearchesViewHolder(binding: ItemSearchSetBinding) :
    RecyclerBasicViewHolder<ItemSearchSetBinding>(
        binding
    ) {

    fun setSearch(
        search: SjSearchWithTags,
        clickOperation: (String, List<SjTag>) -> Unit
    ) {
        binding.search = search
        itemView.setOnClickListener {
            clickOperation(search.search.keyword, search.tags)
        }
    }

}
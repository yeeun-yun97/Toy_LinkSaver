package com.github.yeeun_yun97.toy.linksaver.ui.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.yeeun_yun97.clone.ynmodule.ui.adapter.RecyclerBasicAdapter
import com.github.yeeun_yun97.clone.ynmodule.ui.adapter.RecyclerBasicViewHolder
import com.github.yeeun_yun97.toy.linksaver.data.model.SjSearchWithTags
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.databinding.ItemSearchSetBinding

class SearchSetAdapter(
    private val setSearchOperation: (String, List<SjTag>) -> Unit,
    private val searchStartOperation: () -> Unit
) : RecyclerBasicAdapter<SjSearchWithTags, SearchSetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchSetViewHolder {
        val binding =
            ItemSearchSetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchSetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchSetViewHolder, item: SjSearchWithTags) {
        holder.setSearch(item, setSearchOperation, searchStartOperation)
    }


}

class SearchSetViewHolder(binding: ItemSearchSetBinding) :
    RecyclerBasicViewHolder<ItemSearchSetBinding>(binding) {

    fun setSearch(
        search: SjSearchWithTags,
        setSearchOperation: (String, List<SjTag>) -> Unit,
        searchStartOperation: () -> Unit
    ) {
        binding.search = search
        itemView.setOnClickListener {
            setSearchOperation(search.search.keyword, search.tags)
            searchStartOperation()
        }
        itemView.setOnLongClickListener {
            setSearchOperation(search.search.keyword, search.tags)
            true
        }
    }


}

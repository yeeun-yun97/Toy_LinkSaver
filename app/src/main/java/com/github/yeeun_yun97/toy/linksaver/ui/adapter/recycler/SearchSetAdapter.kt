package com.github.yeeun_yun97.toy.linksaver.ui.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.yeeun_yun97.clone.ynmodule.ui.adapter.YnBaseAdapter
import com.github.yeeun_yun97.clone.ynmodule.ui.adapter.YnBaseViewHolder
import com.github.yeeun_yun97.toy.linksaver.data.model.SjSearchWithTags
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.databinding.ItemSearchSetBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchSetAdapter(
    private val setSearchOperation: (String, List<SjTag>) -> Job,
    private val searchStartOperation: () -> Unit
) : YnBaseAdapter<SjSearchWithTags, SearchSetViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchSetViewHolder {
        val binding =
            ItemSearchSetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchSetViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SearchSetViewHolder,
        item: SjSearchWithTags,
        position: Int
    ) {
        holder.setSearch(item, setSearchOperation, searchStartOperation)
    }


}

class SearchSetViewHolder(binding: ItemSearchSetBinding) :
    YnBaseViewHolder<ItemSearchSetBinding>(binding) {

    fun setSearch(
        search: SjSearchWithTags,
        setSearchOperation: (String, List<SjTag>) -> Job,
        searchStartOperation: () -> Unit
    ) {
        binding.search = search
        itemView.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val setJob = setSearchOperation(search.search.keyword, search.tags)
                launch {
                    setJob.join()
                    searchStartOperation()
                }
            }
        }
        itemView.setOnLongClickListener {
            setSearchOperation(search.search.keyword, search.tags)
            true
        }
    }


}

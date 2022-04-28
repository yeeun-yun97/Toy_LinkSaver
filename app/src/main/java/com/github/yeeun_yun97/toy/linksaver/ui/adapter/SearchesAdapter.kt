package com.github.yeeun_yun97.toy.linksaver.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLink
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLinksAndDomainsWithTags
import com.github.yeeun_yun97.toy.linksaver.data.model.SjSearchWithTags
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.databinding.ItemLinksBinding
import com.github.yeeun_yun97.toy.linksaver.databinding.ItemSearchSetBinding

class SearchesAdapter() :
    RecyclerView.Adapter<SearchesViewHolder>() {
    var itemList: List<SjSearchWithTags> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchesViewHolder {
        val binding = ItemSearchSetBinding.inflate(LayoutInflater.from(parent.context))
        return SearchesViewHolder(binding);
    }

    override fun onBindViewHolder(holder: SearchesViewHolder, position: Int) {
        holder.setSearch(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

}

class SearchesViewHolder(private val binding: ItemSearchSetBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private lateinit var item: SjSearchWithTags

    fun setSearch(
        item: SjSearchWithTags
    ) {
        this.item = item
        binding.search = item
    }
}
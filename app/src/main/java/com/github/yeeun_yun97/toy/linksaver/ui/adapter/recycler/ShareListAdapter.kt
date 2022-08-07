package com.github.yeeun_yun97.toy.linksaver.ui.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.yeeun_yun97.clone.ynmodule.ui.adapter.YnBaseAdapter
import com.github.yeeun_yun97.clone.ynmodule.ui.adapter.YnBaseViewHolder
import com.github.yeeun_yun97.toy.linksaver.databinding.ItemSelectedEntitiesBinding

class ShareListAdapter :
    YnBaseAdapter<Pair<String, Int>, ShareViewHolder>() {
    override fun onBindViewHolder(
        holder: ShareViewHolder,
        item: Pair<String, Int>,
        position: Int
    ) {
        holder.setItem(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShareViewHolder {
        val binding =
            ItemSelectedEntitiesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShareViewHolder(binding)
    }


}

class ShareViewHolder(binding: ItemSelectedEntitiesBinding) :
    YnBaseViewHolder<ItemSelectedEntitiesBinding>(binding) {
    fun setItem(item: Pair<String, Int>) {
        binding.itemNameTextView.setText(item.first)
        binding.itemQuantityTextView.setText("${item.second}개")
    }


}
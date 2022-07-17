package com.github.yeeun_yun97.toy.linksaver.ui.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.yeeun_yun97.clone.ynmodule.ui.adapter.YnBaseAdapter
import com.github.yeeun_yun97.clone.ynmodule.ui.adapter.YnBaseViewHolder
import com.github.yeeun_yun97.toy.linksaver.data.model.SettingItemValue
import com.github.yeeun_yun97.toy.linksaver.databinding.ItemSettingBinding

class SettingListAdapter :
    YnBaseAdapter<SettingItemValue, SettingViewHolder>() {
    override fun onBindViewHolder(
        holder: SettingViewHolder,
        item: SettingItemValue,
        position: Int
    ) {
        holder.setItem(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SettingViewHolder {
        val binding = ItemSettingBinding.inflate(LayoutInflater.from(parent.context))
        return SettingViewHolder(binding)
    }
}

class SettingViewHolder(binding: ItemSettingBinding) :
    YnBaseViewHolder<ItemSettingBinding>(binding) {
    fun setItem(item: SettingItemValue) {
        binding.item = item
        binding.root.setOnClickListener { item.open() }
    }
}


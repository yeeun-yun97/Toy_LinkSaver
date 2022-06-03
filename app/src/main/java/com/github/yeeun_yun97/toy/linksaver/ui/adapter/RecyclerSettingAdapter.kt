package com.github.yeeun_yun97.toy.linksaver.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.yeeun_yun97.clone.ynmodule.ui.adapter.RecyclerBasicAdapter
import com.github.yeeun_yun97.clone.ynmodule.ui.adapter.RecyclerBasicViewHolder
import com.github.yeeun_yun97.toy.linksaver.data.model.SettingItemValue
import com.github.yeeun_yun97.toy.linksaver.databinding.ItemSettingBinding

class RecyclerSettingAdapter :
    RecyclerBasicAdapter<SettingItemValue, RecyclerSettingViewHolder>() {
    override fun onBindViewHolder(
        holder: RecyclerSettingViewHolder,
        item: SettingItemValue
    ) {
        holder.setItem(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerSettingViewHolder {
        val binding = ItemSettingBinding.inflate(LayoutInflater.from(parent.context))
        return RecyclerSettingViewHolder(binding)
    }
}

class RecyclerSettingViewHolder(binding: ItemSettingBinding) :
    RecyclerBasicViewHolder<ItemSettingBinding>(binding) {
        fun setItem(item: SettingItemValue){
            binding.item = item
            binding.root.setOnClickListener { item.open() }
        }
}


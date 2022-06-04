package com.github.yeeun_yun97.toy.linksaver.ui.adapter.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.yeeun_yun97.clone.ynmodule.ui.adapter.RecyclerBasicAdapter
import com.github.yeeun_yun97.clone.ynmodule.ui.adapter.RecyclerBasicViewHolder
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTagGroupWithTags
import com.github.yeeun_yun97.toy.linksaver.databinding.ItemTagGroupBinding

class TagGroupListAdapter(
    val swapOperation: (Int) -> Unit
) : RecyclerBasicAdapter<SjTagGroupWithTags, TagGroupListViewHolder>() {
    override fun onBindViewHolder(holder: TagGroupListViewHolder, item: SjTagGroupWithTags) {
        holder.setItem(item, swapOperation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagGroupListViewHolder {
        val binding =
            ItemTagGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TagGroupListViewHolder(binding)
    }
}

class TagGroupListViewHolder(binding: ItemTagGroupBinding) :
    RecyclerBasicViewHolder<ItemTagGroupBinding>(binding) {

    fun setItem(item: SjTagGroupWithTags, swapOperation: (Int) -> Unit) {
        binding.item = item

        if (item.tagGroup.gid == 1) {
            binding.swapImageView.visibility = View.INVISIBLE
        } else {
            binding.swapImageView.visibility = View.VISIBLE
            binding.swapImageView.setOnClickListener {
                swapOperation(item.tagGroup.gid)
            }
        }

        if (item.tags.isEmpty()) {
            binding.emptyTextView.visibility = View.VISIBLE
        } else {
            binding.emptyTextView.visibility = View.GONE
        }


    }

}
package com.github.yeeun_yun97.toy.linksaver.ui.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.yeeun_yun97.clone.ynmodule.ui.adapter.YnBaseAdapter
import com.github.yeeun_yun97.clone.ynmodule.ui.adapter.YnBaseViewHolder
import com.github.yeeun_yun97.toy.linksaver.data.model.SjDomain
import com.github.yeeun_yun97.toy.linksaver.databinding.ItemDomainsBinding

class DomainListAdapter(
    private val updateOperation: (Int) -> Unit,
    private val deleteOperation: (SjDomain) -> Unit
) : YnBaseAdapter<SjDomain, DomainListViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DomainListViewHolder {
        val binding = ItemDomainsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DomainListViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: DomainListViewHolder,
        item: SjDomain,
        position: Int
    ) {
        holder.setItem(item, updateOperation, deleteOperation)
    }


}

class DomainListViewHolder(binding: ItemDomainsBinding) :
    YnBaseViewHolder<ItemDomainsBinding>(binding) {
    fun setItem(
        domain: SjDomain,
        updateOperation: (Int) -> Unit,
        deleteOperation: (SjDomain) -> Unit
    ) {
        binding.domain = domain
        binding.deleteImageView.setOnClickListener { deleteOperation(domain) }
        binding.editImageView.setOnClickListener { updateOperation(domain.did) }
    }


}
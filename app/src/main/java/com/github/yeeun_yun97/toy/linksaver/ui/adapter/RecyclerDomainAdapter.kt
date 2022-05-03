package com.github.yeeun_yun97.toy.linksaver.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.yeeun_yun97.clone.ynmodule.ui.adapter.RecyclerBasicAdapter
import com.github.yeeun_yun97.clone.ynmodule.ui.adapter.RecyclerBasicViewHolder
import com.github.yeeun_yun97.toy.linksaver.data.model.SjDomain
import com.github.yeeun_yun97.toy.linksaver.databinding.ItemDomainsBinding

class RecyclerDomainAdapter(
    private val updateOperation: (Int) -> Unit,
    private val deleteOperation: (SjDomain) -> Unit
) : RecyclerBasicAdapter<SjDomain, DomainsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DomainsViewHolder {
        val binding = ItemDomainsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DomainsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DomainsViewHolder, item: SjDomain) {
        holder.setItem(item,updateOperation, deleteOperation)
    }

}

class DomainsViewHolder(binding: ItemDomainsBinding) :
    RecyclerBasicViewHolder<ItemDomainsBinding>(binding) {

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
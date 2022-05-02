package com.github.yeeun_yun97.toy.linksaver.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.yeeun_yun97.toy.linksaver.data.model.SjDomain
import com.github.yeeun_yun97.toy.linksaver.databinding.ItemDomainsBinding

class DomainsAdapter(
    private var list: List<SjDomain>,
    private val updateOperation: (Int) -> Unit,
    private val deleteOperation: (SjDomain) -> Unit
) : RecyclerView.Adapter<DomainsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DomainsViewHolder {
        val binding = ItemDomainsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DomainsViewHolder(binding)
    }

    fun setList(list: List<SjDomain>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: DomainsViewHolder, position: Int) {
        holder.setItem(list[position], updateOperation, deleteOperation)
    }

    override fun getItemCount(): Int = list.size
}

class DomainsViewHolder(private val binding: ItemDomainsBinding) :
    RecyclerView.ViewHolder(binding.root) {

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
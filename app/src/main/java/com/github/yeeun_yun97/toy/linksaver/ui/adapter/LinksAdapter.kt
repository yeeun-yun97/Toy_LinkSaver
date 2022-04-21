package com.github.yeeun_yun97.toy.linksaver.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLinkAndDomain
import com.github.yeeun_yun97.toy.linksaver.databinding.ItemLinksBinding

class LinksAdapter(
    private val openOperation: (String) -> Unit
) :
    RecyclerView.Adapter<LinksViewHolder>() {
    var itemList = ArrayList<SjLinkAndDomain>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinksViewHolder {
        val binding = ItemLinksBinding.inflate(LayoutInflater.from(parent.context))
        return LinksViewHolder(binding);
    }

    override fun onBindViewHolder(holder: LinksViewHolder, position: Int) {
        holder.setLink(itemList[position], openOperation)
    }

    override fun getItemCount(): Int = itemList.size

}

class LinksViewHolder(private val binding: ItemLinksBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private lateinit var item: SjLinkAndDomain

    fun setLink(item: SjLinkAndDomain, openOperation: (String) -> Unit) {
        this.item = item
        binding.linksItemDomainTextView.setText(item.domain.name)
        binding.linksItemNameTextView.setText(item.link.name)
        binding.linksItemWebButton.setOnClickListener { openOperation("${item.domain.url}${item.link.url}") }
        binding.linksItemEditButton.setOnClickListener { editLink() }
        binding.linksItemDeleteButton.setOnClickListener { deleteLink() }
    }

    private fun editLink() {

    }

    private fun deleteLink() {

    }
}
package com.github.yeeun_yun97.toy.linksaver.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLink
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLinkAndDomain
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLinksAndDomainsWithTags
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.databinding.ItemLinksBinding

class LinksAdapter(
    private val openOperation: (String) -> Unit,
    private val deleteOperation: (SjLink,List<SjTag>) -> Unit
) :
    RecyclerView.Adapter<LinksViewHolder>() {
    var itemList: List<SjLinksAndDomainsWithTags> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinksViewHolder {
        val binding = ItemLinksBinding.inflate(LayoutInflater.from(parent.context))
        return LinksViewHolder(binding);
    }

    override fun onBindViewHolder(holder: LinksViewHolder, position: Int) {
        holder.setLink(itemList[position], openOperation, deleteOperation)
    }

    override fun getItemCount(): Int = itemList.size

}

class LinksViewHolder(private val binding: ItemLinksBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private lateinit var item: SjLinksAndDomainsWithTags

    fun setLink(
        item: SjLinksAndDomainsWithTags,
        openOperation: (String) -> Unit,
        deleteOperation: (SjLink,List<SjTag>) -> Unit
    ) {
        this.item = item
        Log.d("아이템 내용", item.toString())
        binding.linksItemDomainTextView.setText(item.domain.name)
        binding.linksItemNameTextView.setText(item.link.name)
        binding.linksItemWebButton.setOnClickListener { openOperation("${item.domain.url}${item.link.url}") }
        binding.linksItemEditButton.setOnClickListener { editLink() }
        binding.linksItemDeleteButton.setOnClickListener { deleteOperation(item.link,item.tags) }
    }

    private fun editLink() {

    }

}
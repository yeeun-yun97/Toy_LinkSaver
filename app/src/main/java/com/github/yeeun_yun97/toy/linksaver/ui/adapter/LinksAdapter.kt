package com.github.yeeun_yun97.toy.linksaver.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLink
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLinkWithTags
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLinkWithTagsAndDomain
import com.github.yeeun_yun97.toy.linksaver.databinding.ItemLinksBinding

class LinksAdapter(private val itemList: LiveData<ArrayList<SjLinkWithTagsAndDomain>>, private val openOperation: (String)->Unit) :
    RecyclerView.Adapter<LinksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinksViewHolder {
        val binding = ItemLinksBinding.inflate(LayoutInflater.from(parent.context))
        return LinksViewHolder(binding);
    }

    override fun onBindViewHolder(holder: LinksViewHolder, position: Int) {
        holder.setLink(itemList.value!![position],openOperation)
    }
    override fun getItemCount(): Int = itemList.value!!.size

}

class LinksViewHolder(private val binding:ItemLinksBinding) : RecyclerView.ViewHolder(binding.root) {
    private lateinit var item: SjLinkWithTagsAndDomain

    fun setLink(item: SjLinkWithTagsAndDomain, openOperation: (String) -> Unit) {
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
package com.github.yeeun_yun97.toy.linksaver

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.github.yeeun_yun97.toy.linksaver.databinding.ItemLinksBinding

class LinksAdapter(private val itemList: LiveData<ArrayList<SjLink>>, private val openOperation: (String)->Unit) :
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
    private lateinit var item: SjLink

    fun setLink(item: SjLink, openOperation: (String) -> Unit) {
        this.item = item
        binding.linksItemDomainTextView.setText(item.domain.name)
        binding.linksItemNameTextView.setText(item.name)
        binding.linksItemWebButton.setOnClickListener { openOperation(item.fullUrl) }
        binding.linksItemEditButton.setOnClickListener { editLink() }
        binding.linksItemDeleteButton.setOnClickListener { deleteLink() }
    }

    private fun editLink() {

    }

    private fun deleteLink() {

    }
}
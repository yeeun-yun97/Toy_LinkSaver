package com.github.yeeun_yun97.toy.linksaver

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LinksAdapter(private val itemList: ArrayList<SjLink>) :
    RecyclerView.Adapter<LinksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_links, parent, false)
        return LinksViewHolder(view);
    }

    override fun onBindViewHolder(holder: LinksViewHolder, position: Int) {
        holder.setLink(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

}

class LinksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val domainTextView: TextView by lazy { itemView.findViewById(R.id.linksItem_domainTextView) }
    private val nameTextView: TextView by lazy { itemView.findViewById(R.id.linksItem_nameTextView) }
    private val webButton: ImageView by lazy { itemView.findViewById(R.id.linksItem_webButton) }
    private val editButton: ImageView by lazy { itemView.findViewById(R.id.linksItem_editButton) }
    private val deleteButton: ImageView by lazy { itemView.findViewById(R.id.linksItem_deleteButton) }
    private lateinit var item: SjLink

    fun setLink(item: SjLink) {
        this.item = item
        domainTextView.setText(item.domain.name)
        nameTextView.setText(item.name)
        webButton.setOnClickListener { openWeb(item) }
        editButton.setOnClickListener { editLink() }
        deleteButton.setOnClickListener { deleteLink() }
    }

    private fun openWeb(item:SjLink) {
        Log.i("debug", "${item.domain.url}${item.url}")
    }

    private fun editLink() {

    }

    private fun deleteLink() {

    }
}
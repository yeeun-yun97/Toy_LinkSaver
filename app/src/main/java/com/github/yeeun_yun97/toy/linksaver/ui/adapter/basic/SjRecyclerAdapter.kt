package com.github.yeeun_yun97.toy.linksaver.ui.adapter.basic

import androidx.recyclerview.widget.RecyclerView
import com.github.yeeun_yun97.toy.linksaver.ui.adapter.SearchesViewHolder

abstract class SjRecyclerAdapter<T, C : RecyclerView.ViewHolder>(
    protected var itemList: List<T> = listOf()
) : RecyclerView.Adapter<C>() {

    fun setList(newList: List<T>) {
        this.itemList = newList
        notifyDataSetChanged()
    }

    final override fun onBindViewHolder(holder: C, position: Int) {
        onBindViewHolder(holder,itemList[position])
    }

    abstract fun onBindViewHolder(holder: C, item: T)

    final override fun getItemCount(): Int = itemList.size

}
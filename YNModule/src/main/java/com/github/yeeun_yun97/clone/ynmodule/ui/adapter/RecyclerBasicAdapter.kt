package com.github.yeeun_yun97.clone.ynmodule.ui.adapter

import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerBasicAdapter<T, C : RecyclerView.ViewHolder>(
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
package com.github.yeeun_yun97.clone.ynmodule.ui.adapter

import androidx.recyclerview.widget.RecyclerView

abstract class YnBaseAdapter<D, VH : RecyclerView.ViewHolder>(
    protected var itemList: List<D> = listOf()
) : RecyclerView.Adapter<VH>() {

    fun setList(newList: List<D>) {
        this.itemList = newList
        notifyDataSetChanged()
    }

    final override fun onBindViewHolder(holder: VH, position: Int) {
        onBindViewHolder(holder, itemList[position], position)
    }

    abstract fun onBindViewHolder(holder: VH, item: D, position:Int)

    final override fun getItemCount(): Int = itemList.size

}
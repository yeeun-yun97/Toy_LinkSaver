package com.github.yeeun_yun97.clone.ynmodule.ui.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerBasicViewHolder<T:ViewDataBinding>(protected val binding: T) :
    RecyclerView.ViewHolder(binding.root) {
}
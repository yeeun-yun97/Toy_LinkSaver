package com.github.yeeun_yun97.clone.ynmodule.ui.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class YnBaseViewHolder<B:ViewDataBinding>(protected val binding: B) :
    RecyclerView.ViewHolder(binding.root) {
}
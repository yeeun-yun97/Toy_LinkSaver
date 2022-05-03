package com.github.yeeun_yun97.toy.linksaver.ui.adapter.basic

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class SjDataBindingViewHolder<T:ViewDataBinding>(protected val binding: T) :
    RecyclerView.ViewHolder(binding.root)
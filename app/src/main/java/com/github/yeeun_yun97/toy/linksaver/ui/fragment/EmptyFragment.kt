package com.github.yeeun_yun97.toy.linksaver.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentEmptyBinding

class EmptyFragment :Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentEmptyBinding.inflate(inflater).root
    }

}
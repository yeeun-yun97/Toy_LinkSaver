package com.github.yeeun_yun97.toy.linksaver.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentEditTagBinding
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.DataBindingBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.CreateTagViewModel

class EditTagFragment : DataBindingBasicFragment<FragmentEditTagBinding>() {
    val viewModel: CreateTagViewModel by viewModels()

    override fun layoutId(): Int = R.layout.fragment_edit_tag

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding.nameEdtiText.requestFocus()
        binding.saveButton.setOnClickListener {
            insertTag()
        }


        return binding.root
    }

    private fun insertTag() {
        viewModel.insertTag(SjTag(name = binding.nameEdtiText.text.toString()))
        popBack()
    }

}
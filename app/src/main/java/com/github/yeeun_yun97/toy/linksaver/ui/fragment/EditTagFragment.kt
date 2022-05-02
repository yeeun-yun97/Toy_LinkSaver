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
import com.github.yeeun_yun97.toy.linksaver.viewmodel.TagViewModel

class EditTagFragment : DataBindingBasicFragment<FragmentEditTagBinding>() {
    val viewModel: TagViewModel by viewModels()

    companion object {
        fun newInstance(tag: SjTag): EditTagFragment {
            val fragment = EditTagFragment()
            fragment.arguments = Bundle().apply {
                putInt("tid", tag.tid)
            }
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_edit_tag

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel=viewModel
        if(arguments!=null){
            val tid = arguments!!.getInt("tid")
            viewModel.setTag(tid)
        }
        binding.nameEdtiText.requestFocus()
        binding.saveButton.setOnClickListener { insertTag() }
        return binding.root
    }

    private fun insertTag() {
        viewModel.saveTag()
        popBack()
    }

}
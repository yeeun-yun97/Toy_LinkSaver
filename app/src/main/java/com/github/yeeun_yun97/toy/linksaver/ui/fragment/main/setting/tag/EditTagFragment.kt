package com.github.yeeun_yun97.toy.linksaver.ui.fragment.main.setting.tag

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentEditTagBinding
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.tag.TagViewModel

class EditTagFragment : SjBasicFragment<FragmentEditTagBinding>() {
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

    // override methods
    override fun layoutId(): Int = R.layout.fragment_edit_tag

    override fun onCreateView() {
        binding.viewModel = viewModel
        if (arguments != null) {
            loadUpdateData(arguments!!)
            binding.toolbar.toolbarTitle="태그 수정"
        }

        //set focus
        binding.nameEdtiText.requestFocus()

        // handle user click event
        binding.saveButton.setOnClickListener { saveTag() }
    }


    // load and set update data
    private fun loadUpdateData(arguments: Bundle) {
        val tid = arguments.getInt("tid")
        viewModel.setTag(tid)
    }

    // save Tag
    private fun saveTag() {
        viewModel.saveTag()
        popBack()
    }

}
package com.github.yeeun_yun97.toy.linksaver.ui.fragment

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentEditDomainBinding
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.DomainViewModel

class EditDomainFragment : SjBasicFragment<FragmentEditDomainBinding>() {
    val viewModel: DomainViewModel by activityViewModels()

    companion object {
        fun newInstance(did: Int): EditDomainFragment {
            val fragment = EditDomainFragment()
            fragment.arguments = Bundle().apply { putInt("did", did) }
            return fragment
        }
    }


    // override methods
    override fun layoutId(): Int = R.layout.fragment_edit_domain

    override fun onCreateView() {
        binding.viewModel = viewModel
        if (arguments != null) {
            loadUpdateData(arguments!!)
            binding.toolbar.toolbarTitle="도메인 수정"
        }
        binding.saveButton.setOnClickListener { saveDomain() }
    }


    // load and set update data
    private fun loadUpdateData(arguments: Bundle) {
        val did = arguments.getInt("did")
        viewModel.setDomain(did)
    }


    // save domain
    private fun saveDomain() {
        viewModel.saveDomain()
        popBack()
    }

}
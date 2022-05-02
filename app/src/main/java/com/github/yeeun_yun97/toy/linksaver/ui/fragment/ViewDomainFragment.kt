package com.github.yeeun_yun97.toy.linksaver.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.SjDomain
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentViewDomainBinding
import com.github.yeeun_yun97.toy.linksaver.ui.adapter.DomainsAdapter
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.DataBindingBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.DomainViewModel

class ViewDomainFragment :DataBindingBasicFragment<FragmentViewDomainBinding>(){
    val viewModel : DomainViewModel by activityViewModels()

    override fun layoutId(): Int  = R.layout.fragment_view_domain

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = DomainsAdapter(
            listOf(),
            ::moveToEditDomainFragment,
            ::deleteDomain
        )

        viewModel.domains.observe(
            viewLifecycleOwner,{
                adapter.setList(it)
            }
        )

        binding.recyclerView.layoutManager= LinearLayoutManager(requireContext())
        binding.recyclerView.adapter=adapter
    }

    private fun moveToEditDomainFragment(did: Int){
        val fragment = EditDomainFragment.newInstance(did)
        moveToOtherFragment(fragment)
    }
    private fun deleteDomain(domain:SjDomain){
        viewModel.deleteDomain(domain)
    }


}
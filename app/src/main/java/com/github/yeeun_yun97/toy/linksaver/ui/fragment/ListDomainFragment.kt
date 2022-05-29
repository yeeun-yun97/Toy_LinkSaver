package com.github.yeeun_yun97.toy.linksaver.ui.fragment

import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.SjDomain
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentListDomainBinding
import com.github.yeeun_yun97.toy.linksaver.ui.adapter.RecyclerDomainAdapter
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.DomainViewModel

class ListDomainFragment : SjBasicFragment<FragmentListDomainBinding>() {
    val viewModel: DomainViewModel by activityViewModels()


    // override methods
    override fun layoutId(): Int = R.layout.fragment_list_domain

    override fun onCreateView() {
        val handlerMap = hashMapOf<Int, ()->Unit>(R.id.menu_add to ::moveToAddFragment)
        binding.toolbar.setMenu(R.menu.toolbar_menu_add, handlerMap = handlerMap)
        // set recyclerView
        val adapter = RecyclerDomainAdapter(
            ::moveToEditDomainFragment,
            ::deleteDomain
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        // set domain item list
        viewModel.domains.observe(
            viewLifecycleOwner, {
                if (it.isEmpty()) {
                    binding.include.emptyView.visibility = View.VISIBLE
                } else {
                    binding.include.emptyView.visibility = View.GONE
                }
                adapter.setList(it)
            }
        )
    }


    // handle user click event
    private fun moveToEditDomainFragment(did: Int) {
        val fragment = EditDomainFragment.newInstance(did)
        moveToOtherFragment(fragment)
    }

    private fun deleteDomain(domain: SjDomain) {
        viewModel.deleteDomain(domain)
    }

    private fun moveToAddFragment(){
        moveToOtherFragment(EditDomainFragment())
    }

}
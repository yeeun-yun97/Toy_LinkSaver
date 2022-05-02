package com.github.yeeun_yun97.toy.linksaver.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.SjDomain
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentEditDomainBinding
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.DataBindingBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.DomainViewModel

class EditDomainFragment : DataBindingBasicFragment<FragmentEditDomainBinding>() {
    val viewModel: DomainViewModel by viewModels()

    companion object{
        fun newInstance(did:Int):EditDomainFragment{
            val fragment = EditDomainFragment()
            fragment.arguments=Bundle().apply{
                putInt("did",did)
            }
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_edit_domain

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel=viewModel
        if(arguments!=null){
            val did = arguments!!.getInt("did")
            viewModel.setDomain(did)
            Toast.makeText(requireContext(),"did : $did",Toast.LENGTH_LONG).show()
        }

        binding.saveButton.setOnClickListener {
            saveDomain()
        }
        return binding.root
    }

    private fun saveDomain() {
        viewModel.saveDomain()
        popBack()
    }

}
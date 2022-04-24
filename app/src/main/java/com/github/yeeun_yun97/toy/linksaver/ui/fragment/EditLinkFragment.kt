package com.github.yeeun_yun97.toy.linksaver.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentEditLinkBinding
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.ViewBindingBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.ViewLinkViewModel

class EditLinkFragment : ViewBindingBasicFragment<FragmentEditLinkBinding>() {
    private val viewModel: ViewLinkViewModel by activityViewModels()

    override fun layoutId(): Int = R.layout.fragment_edit_link

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        viewModel.domainNames.observe(this, {
            val spinnerArrayAdapter =
                ArrayAdapter(
                    this.context!!,
                    android.R.layout.simple_spinner_dropdown_item,
                    it
                )
            binding.domainSpinner.adapter = spinnerArrayAdapter;
            val listener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    binding.domainDetailTextView.setText(viewModel.domainUrls.value!!.get(position))
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    Log.i(
                        EditLinkFragment@ this.javaClass.canonicalName,
                        "domainSpinner select is null"
                    )
                    binding.domainSpinner.setSelection(0)
                }

            }
            binding.domainSpinner.onItemSelectedListener=listener
        })
        viewModel.loadDomainNames()
    }

}
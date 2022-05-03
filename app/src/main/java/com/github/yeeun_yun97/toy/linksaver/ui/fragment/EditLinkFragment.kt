package com.github.yeeun_yun97.toy.linksaver.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.CompoundButton
import androidx.fragment.app.activityViewModels
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentEditLinkBinding
import com.github.yeeun_yun97.toy.linksaver.ui.adapter.SpinnerDomainAdapter
import com.github.yeeun_yun97.toy.linksaver.ui.component.SjTagChip
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.DataBindingBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.LinkViewModel
import com.github.yeeun_yun97.toy.linksaver.viewmodel.NameMode

class EditLinkFragment : DataBindingBasicFragment<FragmentEditLinkBinding>() {
    private val viewModel: LinkViewModel by activityViewModels()

    companion object {
        fun newInstance(lid: Int): EditLinkFragment {
            val bundle = Bundle().apply {
                putInt("lid", lid)
            }
            val fragment = EditLinkFragment().apply {
                arguments = bundle
            }
            return fragment
        }
    }


    // override methods
    override fun layoutId(): Int = R.layout.fragment_edit_link

    // load and set update data
    private fun loadUpdateData(arguments: Bundle) {
        val lid = arguments.getInt("lid")
        viewModel.setLink(lid)
    }

    override fun onCreateView() {
        binding.viewModel = viewModel
        if (arguments != null) {
            loadUpdateData(arguments!!)
        }
        //add tag chips
        viewModel.tags.observe(viewLifecycleOwner, { addTagsToChipGroupChildren(it) })

        //when domain list changes
        viewModel.domains.observe(viewLifecycleOwner, {
            //set spinner adapter
            binding.domainSpinner.adapter =
                SpinnerDomainAdapter(context = requireContext(), list = it)

            //set spinner select listener
            binding.domainSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {

                    override fun onItemSelected(
                        p0: AdapterView<*>?,
                        p1: View?,
                        position: Int,
                        p3: Long
                    ) {
                        viewModel.selectDomain(position)
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {}
                }

            //set selection if there is selected Domain
            val domain = viewModel.getSelectedDomain()
            if (domain.did != 0) {
                val index = it!!.indexOf(domain)
                binding.domainSpinner.setSelection(index)
            }
        })


        // mode change when user edits name
        binding.nameEditText.setOnKeyListener { _, _, _ ->
            viewModel.mode = NameMode.MODE_USER
            false
        }


        // handle user click
        binding.saveButton.setOnClickListener { saveLink() }
        binding.addDomainTextView.setOnClickListener { moveToEditDomainFragment() }
        binding.addTagTextView.setOnClickListener { moveToEditTagFragment() }
    }


    private fun addTagsToChipGroupChildren(it: List<SjTag>) {
        binding.tagChipGroup.removeAllViews()
        val onCheckListener = CompoundButton.OnCheckedChangeListener { btn, isChecked ->
            val chip = btn as SjTagChip
            if (isChecked) {
                viewModel.selectTag(chip.tag)
            } else {
                viewModel.unselectTag(chip.tag)
            }
        }

        for (tag in it) {
            val chip = SjTagChip(context!!, tag)
            if (viewModel.targetTagList.contains(tag))
                chip.isChecked = true
            chip.setOnCheckedChangeListener(onCheckListener)
            binding.tagChipGroup.addView(chip)
        }
    }

    // handle click methods
    private fun moveToEditTagFragment() {
        moveToOtherFragment(EditTagFragment())
    }

    private fun moveToEditDomainFragment() {
        moveToOtherFragment(EditDomainFragment())
    }

    private fun saveLink() {
        viewModel.saveLink()
        this.requireActivity().finish()
    }

}
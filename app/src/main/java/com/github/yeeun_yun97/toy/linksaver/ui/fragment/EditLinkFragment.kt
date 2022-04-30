package com.github.yeeun_yun97.toy.linksaver.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.CompoundButton
import androidx.fragment.app.activityViewModels
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentEditLinkBinding
import com.github.yeeun_yun97.toy.linksaver.ui.adapter.DomainAdapter
import com.github.yeeun_yun97.toy.linksaver.ui.component.SjTagChip
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.DataBindingBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.CreateLinkViewModel
import com.github.yeeun_yun97.toy.linksaver.viewmodel.NameMode

class EditLinkFragment : DataBindingBasicFragment<FragmentEditLinkBinding>() {
    private val viewModel: CreateLinkViewModel by activityViewModels()

    override fun layoutId(): Int = R.layout.fragment_edit_link

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //set data binding variable
        binding.viewModel=viewModel

        //add tag chips
        viewModel.tags.observe(viewLifecycleOwner, { addTagsToChipGroupChildren(it) })

        //when selected domain changes
        viewModel.domains.observe(viewLifecycleOwner, {
            //set spinner adapter
            binding.domainSpinner.adapter=
                DomainAdapter(context=requireContext(),list=it)

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
        })

        binding.nameEditText.setOnKeyListener { p0, p1, p2 ->
            viewModel.mode = NameMode.MODE_USER
            false
        }


        //onClickListeners
        binding.saveButton.setOnClickListener { saveLink() }
        binding.addDomainTextView.setOnClickListener { moveToEditDomainFragment() }
        binding.addTagTextView.setOnClickListener { moveToEditTagFragment() }
    }

    private fun addTagsToChipGroupChildren(it: List<SjTag>) {
        binding.tagChipGroup.removeAllViews()
        val onCheckListener = CompoundButton.OnCheckedChangeListener { btn, isChecked ->
            val chip = btn as SjTagChip
                if(isChecked){
                    viewModel.selectedTags.add(chip.tag)
                }else{
                    viewModel.selectedTags.remove(chip.tag)
                }
        }

        for (tag in it) {
            val chip = SjTagChip(context!!, tag)
            chip.setOnCheckedChangeListener(onCheckListener)
            binding.tagChipGroup.addView(chip)
        }
    }

    private fun moveToEditTagFragment() {
        moveToOtherFragment(EditTagFragment())
    }

    private fun moveToEditDomainFragment() {
        moveToOtherFragment(EditDomainFragment())
    }

    private fun saveLink() {
        viewModel.insertLink()
        this.requireActivity().finish()
    }

}
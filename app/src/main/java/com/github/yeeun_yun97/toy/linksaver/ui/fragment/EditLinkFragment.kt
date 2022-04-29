package com.github.yeeun_yun97.toy.linksaver.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import androidx.fragment.app.activityViewModels
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLink
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentEditLinkBinding
import com.github.yeeun_yun97.toy.linksaver.ui.component.SjTagChip
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.DataBindingBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.CreateLinkViewModel

class EditLinkFragment : DataBindingBasicFragment<FragmentEditLinkBinding>() {
    private val viewModel: CreateLinkViewModel by activityViewModels()

    override fun layoutId(): Int = R.layout.fragment_edit_link

    fun setDomainDetailTextView() {
        val builder = StringBuilder(viewModel.getSelectedDomainName())
        builder.append(binding.linkEditText.text.toString())
        binding.domainDetailTextView.setText(builder.toString())
        builder.clear()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.domainNames.observe(viewLifecycleOwner, {
            binding.domainSpinner.adapter =
                ArrayAdapter(
                    this.context!!,
                    android.R.layout.simple_spinner_dropdown_item,
                    it
                )
        })

        viewModel.tags.observe(
            viewLifecycleOwner,
            { addTagsToChipGroupChildren(it) }
        )

        viewModel.domains.observe(viewLifecycleOwner, {
            binding.domainSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {

                    override fun onItemSelected(
                        p0: AdapterView<*>?,
                        p1: View?,
                        position: Int,
                        p3: Long
                    ) {
                        viewModel.selectDomain(position)
                        setDomainDetailTextView()
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
//                        Log.i(getClassName(), "domainSpinner select is null")
//                        binding.domainSpinner.setSelection(0)
                    }

                }
        })

        //textChangeListeners= 에딧텍스트 수정할 때 textView에 표시해주는.
        binding.linkEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setDomainDetailTextView()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

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
        viewModel.insertLink(
            SjLink(
                did = -1,
                name = binding.nameEditText.text.toString(),
                url = binding.linkEditText.text.toString()
            )
        )
        this.requireActivity().finish()
    }

}
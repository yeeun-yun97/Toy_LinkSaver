package com.github.yeeun_yun97.toy.linksaver.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLink
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentEditLinkBinding
import com.github.yeeun_yun97.toy.linksaver.ui.component.SjTagChip
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.DataBindingBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.CreateLinkViewModel

class EditLinkFragment : DataBindingBasicFragment<FragmentEditLinkBinding>() {
    private val viewModel: CreateLinkViewModel by activityViewModels()

    override fun layoutId(): Int = R.layout.fragment_edit_link

    fun setDomainDetailTextView() {
        val builder = StringBuilder(viewModel.selectedDomain.url)
        builder.append(binding.linkEditText.text.toString())
        binding.domainDetailTextView.setText(builder.toString())
        builder.clear()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.domains.observe(viewLifecycleOwner, {

            //이거 Dao에서 StringList로 받아오는 방법 혹시 찾을 수 있으면 고치면 좋겠다.
            val nameList = mutableListOf<String>()
            for (tag in it) {
                nameList.add(tag.name)
            }

            binding.domainSpinner.adapter =
                ArrayAdapter(
                    this.context!!,
                    android.R.layout.simple_spinner_dropdown_item,
                    nameList
                );
        })

        viewModel.tags.observe(viewLifecycleOwner, {
            for (tag in it) {
                val chip = SjTagChip(context!!, tag)
                binding.tagChipGroup.addView(chip)
            }
        })
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
                    Log.i(getClassName(), "domainSpinner select is null")
                    binding.domainSpinner.setSelection(0)
                }
            }

        binding.linkEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setDomainDetailTextView()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        binding.saveButton.setOnClickListener {
            viewModel.insertLink(
                SjLink(
                    did = -1,
                    name = binding.nameEditText.text.toString(),
                    url = binding.linkEditText.text.toString()
                )
            )
            requireActivity().finish()
        }
    }


}
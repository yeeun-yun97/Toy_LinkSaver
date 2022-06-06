package com.github.yeeun_yun97.toy.linksaver.ui.fragment.main.setting.tag

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentSwapTagGroupBinding
import com.github.yeeun_yun97.toy.linksaver.ui.component.SjTagChip
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.tag.TagGroupViewModel

class SwapTagGroupFragment : SjBasicFragment<FragmentSwapTagGroupBinding>() {

    private val viewModel: TagGroupViewModel by viewModels()

    companion object {
        fun newInstance(gid: Int): SwapTagGroupFragment {
            val fragment = SwapTagGroupFragment()
            fragment.arguments = Bundle().apply {
                putInt("gid", gid)
            }
            return fragment
        }
    }

    private fun loadTargetTagGroup(){
        val gid = requireArguments().getInt("gid", -1)
        viewModel.setTargetTagGroupGid(gid)
    }

    override fun layoutId(): Int = R.layout.fragment_swap_tag_group

    override fun onCreateView() {
        loadTargetTagGroup()
        binding.viewModel = viewModel

        viewModel.bindingBasicTagGroup.observe(viewLifecycleOwner, {
            binding.basicChipGroup.removeAllViews()
            for (tag in it.tags) {
                val chip = SjTagChip(requireContext(), tag)
                chip.setOnCheckedChangeListener { button, isChecked ->
                    if(isChecked){
                        viewModel.selectedBasicTags.add(tag)
                    }else{
                        viewModel.selectedBasicTags.remove(tag)
                    }
                }
                binding.basicChipGroup.addView(chip)
            }
        })

        viewModel.bindingTargetTagGroup.observe(viewLifecycleOwner, {
            binding.targetChipGroup.removeAllViews()
            for (tag in it.tags) {
                val chip = SjTagChip(requireContext(), tag)
                chip.setOnCheckedChangeListener { button, isChecked ->
                    if(isChecked){
                        viewModel.selectedTargetTags.add(tag)
                    }else{
                        viewModel.selectedTargetTags.remove(tag)
                    }
                }
                binding.targetChipGroup.addView(chip)
            }
        })

        binding.moveToBasicImageView.setOnClickListener { moveSelectedTargetTagsToBasicGroup() }
        binding.moveToTargetImageView.setOnClickListener { moveSelectedBasicTagsToTargetGroup() }
    }

    private fun moveSelectedTargetTagsToBasicGroup() {
        viewModel.moveSelectedTargetTagsToBasicGroup()
    }

    private fun moveSelectedBasicTagsToTargetGroup() {
        viewModel.moveSelectedBasicTagsToTargetGroup()
    }
}
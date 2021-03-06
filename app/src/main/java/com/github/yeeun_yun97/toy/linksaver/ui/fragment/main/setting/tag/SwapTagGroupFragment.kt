package com.github.yeeun_yun97.toy.linksaver.ui.fragment.main.setting.tag

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentSwapTagGroupBinding
import com.github.yeeun_yun97.toy.linksaver.ui.component.SjTagChip
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.tag.SwapTagViewModel

class SwapTagGroupFragment : SjBasicFragment<FragmentSwapTagGroupBinding>() {

    private val viewModel: SwapTagViewModel by viewModels()

    companion object {
        fun newInstance(gid: Int): SwapTagGroupFragment {
            val fragment = SwapTagGroupFragment()
            fragment.arguments = Bundle().apply {
                putInt("gid", gid)
            }
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_swap_tag_group

    override fun onCreateView() {

        // handle arguments
        val gid = requireArguments().getInt("gid", -1)
        viewModel.setTargetTagGroupByGid(gid)

        // set binding variable
        binding.viewModel = viewModel

        // observe basic tag group
        viewModel.bindingBasicTagGroup.observe(viewLifecycleOwner, {
            binding.basicChipGroup.removeAllViews()
            for (tag in it.tags) {
                val chip = SjTagChip(requireContext(), tag)
                chip.setOnCheckedChangeListener { button, isChecked ->
                    if (isChecked) {
                        viewModel.selectedBasicTags.add(tag)
                    } else {
                        viewModel.selectedBasicTags.remove(tag)
                    }
                }
                binding.basicChipGroup.addView(chip)
            }
        })

        viewModel.bindingTargetTagGroup.observe(viewLifecycleOwner, {
            binding.include.tagChipGroup.removeAllViews()
            if (it.tags.isEmpty()) {
                binding.include.emptyTextView.visibility = View.VISIBLE
            } else {
                binding.include.emptyTextView.visibility = View.INVISIBLE
            }
            binding.include.groupNameTextView.setText(it.tagGroup.name)
            binding.include.privateImageView.visibility =
                if (it.tagGroup.isPrivate) View.VISIBLE
                else View.GONE
            for (tag in it.tags) {
                val chip = SjTagChip(requireContext(), tag)
                chip.setOnCheckedChangeListener { button, isChecked ->
                    if (isChecked) {
                        viewModel.selectedTargetTags.add(tag)
                    } else {
                        viewModel.selectedTargetTags.remove(tag)
                    }
                }
                binding.include.tagChipGroup.addView(chip)
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
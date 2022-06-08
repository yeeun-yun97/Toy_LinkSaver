package com.github.yeeun_yun97.toy.linksaver.ui.fragment.edit_link

import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTagGroupWithTags
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentEditLinkBinding
import com.github.yeeun_yun97.toy.linksaver.ui.component.EditTagDialogFragment
import com.github.yeeun_yun97.toy.linksaver.ui.component.SjTagChip
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.edit_link.EditVideoViewModel

class EditLinkAndVideoFragment : SjBasicFragment<FragmentEditLinkBinding>() {

    val viewModel: EditVideoViewModel by activityViewModels()

    companion object {
        fun newInstance(lid: Int, url: String): EditLinkAndVideoFragment {
            val fragment = EditLinkAndVideoFragment()
            fragment.arguments = Bundle().apply {
                putInt("lid", lid)
                putString("url", url)
            }
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_edit_link

    override fun onCreateView() {

        // handle arguments
        val arguments = requireArguments()
        handleArguments(arguments)

        // set binding variable
        binding.viewModel = viewModel

        // set toolbar menu
        val handlerMap = hashMapOf<Int, () -> Unit>(R.id.menu_save to ::saveVideo)
        binding.toolbar.setMenu(R.menu.toolbar_menu_edit, handlerMap)

        // show or hide video icon
        viewModel.bindingIsVideo.observe(viewLifecycleOwner,
            {
                binding.videoTypeIconImageView.visibility =
                    if (it) View.VISIBLE
                    else View.INVISIBLE
            }
        )

        binding.addTagTextView.setOnClickListener {
            showEditTagDialog()
        }

        // set preview image
        viewModel.bindingPreviewImage.observe(viewLifecycleOwner, {
            Glide.with(requireContext())
                .load(it)
                .error(R.drawable.ic_icons8_no_image_100)
                .centerCrop()
                .into(binding.previewImageView)
        })

        // set tagList
        viewModel.tagGroups.observe(viewLifecycleOwner, {
            if(viewModel.tagDefaultGroup.value!=null)
            this.addTagsToChipGroupChildren(viewModel.tagDefaultGroup.value!!, it)
        })
        viewModel.tagDefaultGroup.observe(viewLifecycleOwner, {
            if(viewModel.tagGroups.value!=null)
             this.addTagsToChipGroupChildren(it, viewModel.tagGroups.value!!)
        })

    }

    private fun showEditTagDialog() {
        val dialogFragment = EditTagDialogFragment(::createTag, null)
        dialogFragment.show(childFragmentManager, "그룹 없는 새 태그 생성하기")
    }

    private fun createTag(name: String, tag: SjTag?) = viewModel.createTag(name)

    private fun handleArguments(arguments: Bundle) {
        val lid = arguments.getInt("lid", -1)
        val url = arguments.getString("url") ?: ""

        if (lid != -1) {
            viewModel.setLinkByLid(lid)
        } else {
            viewModel.createLinkByUrl(url)
        }
    }

    private fun addTagsToChipGroupChildren(
        defaultGroup: SjTagGroupWithTags,
        groups: List<SjTagGroupWithTags>
    ) {
        val onCheckListener =
            CompoundButton.OnCheckedChangeListener { btn, isChecked ->
                val chip = btn as SjTagChip
                if (isChecked) {
                    viewModel.selectTag(chip.tag)
                } else {
                    viewModel.unselectTag(chip.tag)
                }
            }

        binding.tagChipGroup.removeAllViews()
        for (def in defaultGroup.tags) {
            val chip = SjTagChip(context!!, def)
            chip.isChecked = viewModel.isTagSelected(def)
            chip.setOnCheckedChangeListener(onCheckListener)
            binding.tagChipGroup.addView(chip)
        }
        for (group in groups) {
            for (tag in group.tags) {
                val chip = SjTagChip(context!!, tag)
                chip.isChecked = viewModel.isTagSelected(tag)
                chip.setOnCheckedChangeListener(onCheckListener)
                chip.setText("${group.tagGroup.name}: ${tag.name}")
                binding.tagChipGroup.addView(chip)
            }
        }
    }

    private fun saveVideo() {
        viewModel.saveVideo()
        this.requireActivity().finish()
    }
}
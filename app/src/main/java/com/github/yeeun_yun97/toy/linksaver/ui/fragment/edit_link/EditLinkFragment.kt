package com.github.yeeun_yun97.toy.linksaver.ui.fragment.edit_link

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
import com.github.yeeun_yun97.toy.linksaver.viewmodel.edit_link.EditLinkViewModel

class EditLinkFragment : SjBasicFragment<FragmentEditLinkBinding>() {

    val viewModel: EditLinkViewModel by activityViewModels()

    private val dialogFragment = EditTagDialogFragment(::createTag, null)

    private val onCheckListener =
        CompoundButton.OnCheckedChangeListener { btn, isChecked ->
            val chip = btn as SjTagChip
            if (isChecked) {
                viewModel.selectTag(chip.tag)
            } else {
                viewModel.unselectTag(chip.tag)
            }
        }

    override fun layoutId(): Int = R.layout.fragment_edit_link

    override fun onStart() {
        super.onStart()
        //TODO refresh
    }

    override fun onCreateView() {
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
            setCheckableTagsToChipGroupChildren(viewModel.tagDefaultGroup.value, it)
        })
        viewModel.tagDefaultGroup.observe(viewLifecycleOwner, {
            setCheckableTagsToChipGroupChildren(it, viewModel.tagGroups.value)
        })

        setOnClickListeners()

    }

    override fun setOnClickListeners() {
        binding.addTagTextView.setOnClickListener {
            showEditTagDialog()
        }
    }

    private fun showEditTagDialog() {
        dialogFragment.show(childFragmentManager, "그룹 없는 새 태그 생성하기")
    }

    private fun createTag(name: String, tag: SjTag?) = viewModel.createTag(name)

    private fun isTagSelected(tag: SjTag) = viewModel.isTagSelected(tag)

    private fun setCheckableTagsToChipGroupChildren(
        defaultGroup: SjTagGroupWithTags?,
        groups: List<SjTagGroupWithTags>?
    ) =
        setTagsToChipGroupChildren(
            defaultGroup,
            groups,
            ::isTagSelected,
            binding.tagChipGroup,
            onCheckListener
        )

    private fun saveVideo() {
        viewModel.saveVideo()
        this.requireActivity().finish()
    }


}
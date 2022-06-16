package com.github.yeeun_yun97.toy.linksaver.ui.fragment.main.setting.tag

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentListTagBinding
import com.github.yeeun_yun97.toy.linksaver.ui.component.EditTagDialogFragment
import com.github.yeeun_yun97.toy.linksaver.ui.component.SjTagChip
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.tag.TagGroupEditViewModel

class EditTagInGroupFragment : SjBasicFragment<FragmentListTagBinding>() {
    val viewModel: TagGroupEditViewModel by activityViewModels()
    private var gid: Int = -1
    private var groupName: String = ""

    companion object {
        fun newInstance(gid: Int): EditTagInGroupFragment {
            val fragment = EditTagInGroupFragment()
            fragment.arguments = Bundle().apply {
                putInt("gid", gid)
            }
            return fragment
        }
    }

    // override methods
    override fun layoutId(): Int = R.layout.fragment_list_tag

    override fun onStart() {
        super.onStart()
        viewModel.setGid(gid)
    }

    override fun onCreateView() {
        gid = requireArguments().getInt("gid", -1)

        val handlerMap = hashMapOf<Int, () -> Unit>(
            R.id.menu_group_new_tag to ::createNewTag,
            R.id.menu_group_swap to ::moveToSwapTagGroupFragment,
        )
        binding.toolbar.setMenu(R.menu.toolbar_menu_tag_group, handlerMap)

        viewModel.tagGroupWithTags.observe(viewLifecycleOwner, {
            groupName = it.tagGroup.name
            binding.toolbar.toolbarTitle = it.tagGroup.name
            if (it.tags.isEmpty()) {
                binding.emptyGroup.visibility = View.VISIBLE
            } else {
                binding.emptyGroup.visibility = View.GONE
            }
            addTagChipsToTagChipGroup(it.tags)
        }
        )
    }

    private fun createNewTag() {
        val tagGroupWithTag = viewModel.tagGroupWithTags.value
        val dialogFragment = EditTagDialogFragment(::editTag, tagGroupWithTag?.tagGroup)
        dialogFragment.show(childFragmentManager, "그룹에 새 태그 생성하기")
    }

    // handle user click event
    private fun renameTag(tag: SjTag) {
        val tagGroupWithTag = viewModel.tagGroupWithTags.value
        val dialogFragment = EditTagDialogFragment(::editTag, tagGroupWithTag?.tagGroup, tag)
        dialogFragment.show(childFragmentManager, "그룹에 새 태그 생성하기")
    }

    private fun editTag(name: String, tag: SjTag?) {
        viewModel.editTag(tag, name, gid)
        viewModel.setGid(gid)
    }

    private fun moveToSwapTagGroupFragment() {
        moveToOtherFragment(SwapTagGroupFragment.newInstance(gid))
    }


    // add chipGroup chips
    private fun addTagChipsToTagChipGroup(it: List<SjTag>) {
        binding.tagChipGroup.removeAllViews()
        for (tag in it) {
            val chip = SjTagChip(requireContext(), tag)
            chip.setCheckableAndLongClickableMode(
                deleteOperation = ::deleteTag,
                editOperation = ::renameTag
            )
            binding.tagChipGroup.addView(chip)
        }
    }

    private fun deleteTag(tag: SjTag) {
        viewModel.deleteTag(tag)
        //TODO
        //여기서 확인 다이얼로그 표시하면 좋을 것 같음.
        //링크와의 연결도 사라진다고 알리기.
    }


}



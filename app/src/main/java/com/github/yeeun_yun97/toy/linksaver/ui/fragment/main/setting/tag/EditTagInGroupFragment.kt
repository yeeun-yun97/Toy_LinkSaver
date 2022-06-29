package com.github.yeeun_yun97.toy.linksaver.ui.fragment.main.setting.tag

import androidx.fragment.app.activityViewModels
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentListTagBinding
import com.github.yeeun_yun97.toy.linksaver.ui.component.EditTagDialogFragment
import com.github.yeeun_yun97.toy.linksaver.ui.component.SjTagChip
import com.github.yeeun_yun97.toy.linksaver.ui.component.TagValue
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.tag.TagGroupEditViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditTagInGroupFragment @Inject constructor() : SjBasicFragment<FragmentListTagBinding>() {
    val viewModel: TagGroupEditViewModel by activityViewModels()

    // override methods
    override fun layoutId(): Int = R.layout.fragment_list_tag

    override fun onResume() {
        super.onResume()
        viewModel.loadTagGroup()
    }

    override fun onCreateView() {
        binding.viewModel = viewModel

        val handlerMap = hashMapOf<Int, () -> Unit>(
            R.id.menu_group_new_tag to ::createNewTag,
            R.id.menu_group_swap to ::moveToSwapTagGroupFragment,
        )
        binding.toolbar.setMenu(R.menu.toolbar_menu_tag_group, handlerMap)

        viewModel.tagGroup.observe(viewLifecycleOwner) {
            binding.toolbar.toolbarTitle = it.tagGroup.name
            addTagChipsToTagChipGroup(it.tags)
        }
    }

    private fun createNewTag() {
        val tagGroupWithTag = viewModel.tagGroup.value
        val dialogFragment = EditTagDialogFragment(::editTag, tagGroupWithTag?.tagGroup)
        dialogFragment.show(childFragmentManager, "새 태그 만들기")
    }

    // handle user click event
    private fun renameTag(tag: SjTag) {
        val tagGroupWithTag = viewModel.tagGroup.value
        val dialogFragment = EditTagDialogFragment(::editTag, tagGroupWithTag?.tagGroup, tag)
        dialogFragment.show(childFragmentManager, "태그 이름 수정하기")
    }

    private fun editTag(name: String, tag: SjTag?) {
        viewModel.editTag(tag, name)
        viewModel.loadTagGroup()
    }

    private fun moveToSwapTagGroupFragment() {
        moveToOtherFragment(SwapTagGroupFragment.newInstance(viewModel.gid))
    }

    // add chipGroup chips
    private fun addTagChipsToTagChipGroup(it: List<SjTag>) {
        binding.tagChipGroup.removeAllViews()
        for (tag in it) {
            val chip = SjTagChip(requireContext())
            chip.setTagValue(TagValue(tag))
            chip.setDeletableAndLongClickableMode(
                deleteOperation = ::deleteTag,
                longClickOperation = ::renameTag
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



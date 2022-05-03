package com.github.yeeun_yun97.toy.linksaver.ui.fragment

import android.view.View
import androidx.fragment.app.activityViewModels
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentViewTagBinding
import com.github.yeeun_yun97.toy.linksaver.ui.component.SjTagChip
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.TagViewModel

class ViewTagFragment : SjBasicFragment<FragmentViewTagBinding>() {
    val viewModel: TagViewModel by activityViewModels()


    // override methods
    override fun layoutId(): Int = R.layout.fragment_view_tag

    override fun onCreateView() {
        viewModel.tags.observe(
            viewLifecycleOwner, {
                if (it.isEmpty()) {
                    binding.include.emptyView.visibility = View.VISIBLE
                } else {
                    binding.include.emptyView.visibility = View.GONE
                }
                addTagChipsToTagChipGroup(it)
            }
        )
    }


    // add chipGroup chips
    private fun addTagChipsToTagChipGroup(it: List<SjTag>) {
        binding.tagChipGroup.removeAllViews()
        for (tag in it) {
            val chip = SjTagChip(requireContext(), tag)
            chip.setEditMode(
                deleteOperation = ::deleteTag,
                editOperation = ::moveToEditTagFragment
            )
            binding.tagChipGroup.addView(chip)
        }
    }


    // handle user click event
    private fun moveToEditTagFragment(tag: SjTag) {
        moveToOtherFragment(EditTagFragment.newInstance(tag))
    }

    private fun deleteTag(tag: SjTag) {
        viewModel.deleteTag(tag)
        //TODO
        //여기서 확인 다이얼로그 표시하면 좋을 것 같음.
        //링크와의 연결도 사라진다고 알리기.
    }

}
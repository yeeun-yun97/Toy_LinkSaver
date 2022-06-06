package com.github.yeeun_yun97.toy.linksaver.ui.fragment.main.setting.tag

import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentListTagGroupBinding
import com.github.yeeun_yun97.toy.linksaver.ui.adapter.recycler.TagGroupListAdapter
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.tag.TagViewModel
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.ui.component.EditTagDialogFragment
import com.github.yeeun_yun97.toy.linksaver.ui.component.EditTagGroupDialogFragment


class ListTagGroupFragment : SjBasicFragment<FragmentListTagGroupBinding>() {
    val viewModel: TagViewModel by activityViewModels()

    // override methods
    override fun layoutId(): Int = R.layout.fragment_list_tag_group

    override fun onCreateView() {
        binding.viewModel = viewModel

        val handlerMap = hashMapOf<Int, () -> Unit>(
            R.id.menu_add_group to ::addTagGroup,
            R.id.menu_add_tag to ::addTag,
        )
        binding.toolbar.setMenu(R.menu.toolbar_menu_tag, handlerMap = handlerMap)

        binding.tagGroupRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = TagGroupListAdapter(
            deleteOperation = ::deleteGroupByGid,
            editOperation = ::moveToEditTagGroupFragment,
        )
        binding.tagGroupRecyclerView.adapter = adapter

        viewModel.tagGroups.observe(viewLifecycleOwner, {
            adapter.setList(it)
        })


    }


    private fun moveToEditTagGroupFragment(gid: Int) {
        moveToOtherFragment(EditTagGroupFragment.newInstance(gid))
    }

    private fun deleteGroupByGid(gid: Int) {
        viewModel.deleteTagGroup(gid)
    }

    private fun addTagGroup() {
        val dialogFragment = EditTagGroupDialogFragment(::createTagGroup)
        dialogFragment.show(childFragmentManager, "새 태그 그룹 생성하기")
    }

    private fun createTagGroup(name: String, isPrivate: Boolean) =
        viewModel.createTagGroup(name, isPrivate)

    private fun createTag(tag: SjTag?, name: String) = viewModel.createTag(name)

    private fun addTag() {
        val dialogFragment = EditTagDialogFragment(::createTag, null)
        dialogFragment.show(childFragmentManager, "그룹 없는 새 태그 생성하기")
    }


}
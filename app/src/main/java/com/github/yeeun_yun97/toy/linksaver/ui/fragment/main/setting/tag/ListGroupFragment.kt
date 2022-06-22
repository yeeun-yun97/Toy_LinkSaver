package com.github.yeeun_yun97.toy.linksaver.ui.fragment.main.setting.tag

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentListTagGroupBinding
import com.github.yeeun_yun97.toy.linksaver.ui.adapter.recycler.TagGroupListAdapter
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.tag.ListGroupViewModel
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTagGroup
import com.github.yeeun_yun97.toy.linksaver.ui.component.EditTagGroupDialogFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.tag.TagGroupEditViewModel

class ListGroupFragment : SjBasicFragment<FragmentListTagGroupBinding>() {
    private val viewModel: ListGroupViewModel by activityViewModels()

    private val editGroupFragment = EditTagInGroupFragment()
    private val editGroupViewModel: TagGroupEditViewModel by activityViewModels()

    private val adapter = TagGroupListAdapter(
        deleteOperation = ::deleteGroupByGid,
        editOperation = ::moveToEditTagGroupFragment,
        renameOperation = ::showEditTagGroupDialogToEdit
    )

    // override methods
    override fun layoutId(): Int = R.layout.fragment_list_tag_group

    override fun onStart() {
        super.onStart()
        viewModel.refreshData()
    }

    override fun onCreateView() {
        // set binding variable
        binding.viewModel = viewModel

        // set toolbar menu
        val handlerMap = hashMapOf<Int, () -> Unit>(
            R.id.menu_add_group to ::showEditTagGroupDialog
        )
        binding.toolbar.setMenu(R.menu.toolbar_menu_tag, handlerMap = handlerMap)

        // set recyclerView
        initRecyclerView()

        viewModel.bindingTagGroups.observe(viewLifecycleOwner) {
            adapter.setList(it)
        }
    }

    override fun initRecyclerView() {
        binding.tagGroupRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.tagGroupRecyclerView.adapter = adapter
    }

    private fun showEditTagGroupDialog() {
        val dialogFragment = EditTagGroupDialogFragment(::createTagGroup, null)
        dialogFragment.show(childFragmentManager, "새 태그 그룹 생성하기")
    }

    private fun createTagGroup(name: String, isPrivate: Boolean, group: SjTagGroup? = null) =
        viewModel.editTagGroup(name, isPrivate, group)


    private fun moveToEditTagGroupFragment(gid: Int) {
        editGroupViewModel.gid = gid
        moveToOtherFragment(editGroupFragment)
    }

    private fun showEditTagGroupDialogToEdit(tagGroup: SjTagGroup) {
        val dialogFragment = EditTagGroupDialogFragment(::createTagGroup, tagGroup)
        dialogFragment.show(childFragmentManager, "태그 그룹 수정하기")
    }

    private fun deleteGroupByGid(gid: Int) {
        viewModel.deleteTagGroup(gid)
    }


}
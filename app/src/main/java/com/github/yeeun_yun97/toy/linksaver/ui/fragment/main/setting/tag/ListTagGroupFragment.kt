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
import android.widget.Switch
import androidx.appcompat.app.AlertDialog


class ListTagGroupFragment : SjBasicFragment<FragmentListTagGroupBinding>() {
    val viewModel: TagViewModel by activityViewModels()

    // override methods
    override fun layoutId(): Int = R.layout.fragment_list_tag_group

    override fun onCreateView() {
        val handlerMap = hashMapOf<Int, () -> Unit>(R.id.menu_add to ::addTagGroup)
        binding.toolbar.setMenu(R.menu.toolbar_menu_add, handlerMap = handlerMap)

        binding.tagGroupRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = TagGroupListAdapter(::moveToSwapTagGroupFragment)
        binding.tagGroupRecyclerView.adapter = adapter

        viewModel.tagGroups.observe(viewLifecycleOwner, {
            adapter.setList(it)
        })

    }

    private fun moveToSwapTagGroupFragment(gid: Int) {
        moveToOtherFragment(SwapTagGroupFragment.newInstance(gid))
    }

    private fun addTagGroup() {
        Toast.makeText(requireContext(), "태그 그룹 생성", Toast.LENGTH_LONG).show()
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("태그 그룹 생성하기")

        // Set up the input
        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

//        val switch = Switch(requireContext())
//        switch.setText("비공개 모드")
//        switch.isChecked = false
//        builder.setView(switch)

        // Set up the buttons
        builder.setPositiveButton("OK",
            { dialog, which ->
                val name = input.text.toString()
                val isPrivate = false
                viewModel.createTagGroup(name, isPrivate)
            })
        builder.setNegativeButton("Cancel",
            { dialog, which -> dialog.cancel() })
        builder.show()
    }


}
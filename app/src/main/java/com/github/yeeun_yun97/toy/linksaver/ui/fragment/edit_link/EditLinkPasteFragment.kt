package com.github.yeeun_yun97.toy.linksaver.ui.fragment.edit_link

import android.content.ClipDescription
import android.view.View
import android.widget.Toast
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentEditPasteBinding
import com.github.yeeun_yun97.toy.linksaver.ui.component.LinkPasteBottomSheet
import com.github.yeeun_yun97.toy.linksaver.ui.component.SjClipboard
import com.github.yeeun_yun97.toy.linksaver.ui.component.SjUtil
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment

class EditLinkPasteFragment : SjBasicFragment<FragmentEditPasteBinding>() {

    private var bottomSheet :LinkPasteBottomSheet? = null

    override fun layoutId(): Int = R.layout.fragment_edit_paste

    override fun onCreateView() {
        val pasteHandler = View.OnClickListener {
            openClipBoard()
        }
        binding.pasteImageView.setOnClickListener(pasteHandler)
        binding.pasteTextView.setOnClickListener(pasteHandler)
    }

    private fun openClipBoard() {
        val clipboard = SjClipboard.getClipboardManager(requireContext())

        if (clipboard.hasPrimaryClip()) {
            val clip = clipboard.primaryClip!!
            val description = clipboard.primaryClipDescription!!

            SjClipboard.debugClipMimeType(description)

            val item = clip.getItemAt(0)
            if (description.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                val text = item.text.toString()
                showBottomSheet(text)
            } else {
                try {
                    // sometimes chrome copy url without mimetype.
                    val text = item.text.toString()
                    showBottomSheet(text)
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "클립의 형식이 틀립니다.", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Toast.makeText(requireContext(), "클립보드가 비어있습니다.", Toast.LENGTH_LONG).show()
        }
    }

    private fun showBottomSheet(url: String) {
        bottomSheet = LinkPasteBottomSheet.newInstance(url, ::moveToPreviewFragment)
        bottomSheet!!.show(parentFragmentManager, "TAG")
    }

    private fun moveToPreviewFragment(text: String) {
        if (SjUtil.checkUrlPrefix(text)) {
            bottomSheet?.dismiss()
            moveToOtherFragment(EditLinkAndVideoFragment.newInstance(-1, text))
        } else {
            Toast.makeText(requireContext(), "url 형식이 아닙니다.", Toast.LENGTH_LONG).show()
        }
    }
}
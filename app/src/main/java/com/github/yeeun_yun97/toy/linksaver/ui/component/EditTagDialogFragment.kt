package com.github.yeeun_yun97.toy.linksaver.ui.component

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTagGroup
import java.lang.IllegalStateException

class EditTagDialogFragment(
    private val saveOperation: (SjTag?, String) -> Unit,
    private val group: SjTagGroup? = null,
    private val tag: SjTag? = null
) :
    DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val dialogView = inflater.inflate(R.layout.dialog_edit_tag, null)

            val nameEditText = dialogView.findViewById<EditText>(R.id.tagNameTextView)
            val groupNameTextView = dialogView.findViewById<TextView>(R.id.groupNameTextView)

            builder.setTitle("새로운 태그 만들기")
            if (group != null) {
                groupNameTextView.setText(group.name)
            }
            if (tag != null) {
                nameEditText.setText(tag.name)
            }

            builder.setView(dialogView)
                .setPositiveButton("확인") { _, _ ->
                    saveOperation(tag, nameEditText.text.toString())
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
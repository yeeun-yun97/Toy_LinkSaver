package com.github.yeeun_yun97.toy.linksaver.ui.component

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.DialogFragment
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTagGroup

class EditTagGroupDialogFragment(
    private val saveOperation: (String, Boolean,SjTagGroup?) -> Unit,
    private val tagGroup: SjTagGroup? = null
) :
    DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val dialogView = inflater.inflate(R.layout.dialog_edit_tag_group, null)

            val nameEditText = dialogView.findViewById<EditText>(R.id.groupNameEditText)
            val isPrivateSwitch = dialogView.findViewById<SwitchCompat>(R.id.isPrivateSwitch)

            builder.setTitle("새로운 태그 그룹 만들기")
            if(tagGroup!=null){
                nameEditText.setText(tagGroup.name)
                isPrivateSwitch.isChecked=tagGroup.isPrivate
            }

            builder.setView(dialogView)
                .setPositiveButton("확인") { _, _ ->
                    saveOperation(
                        nameEditText.text.toString(),
                        isPrivateSwitch.isChecked,
                        tagGroup
                    )
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
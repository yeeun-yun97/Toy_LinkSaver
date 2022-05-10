package com.github.yeeun_yun97.toy.linksaver.ui.component

import android.content.Context
import androidx.core.content.ContextCompat
import com.github.yeeun_yun97.clone.ynmodule.ui.component.BasicChipUtil
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.google.android.material.chip.Chip

class SjTagChip(context: Context, val tag: SjTag) : Chip(context, null) {

    init {
        setText(tag.name)
        setBasicMode()
        isCheckedIconVisible = false

        BasicChipUtil.setBasicColorStyle(this)
    }

    fun setBasicMode() {
        isCheckable = true
        id = tag.tid
    }

    fun setEditMode(
        deleteOperation: (SjTag) -> Unit,
        editOperation: (SjTag) -> Unit
    ) {
        isCheckable = false
        isCloseIconVisible = true
        setOnCloseIconClickListener {
            deleteOperation(tag)
        }
        setOnLongClickListener {
            editOperation(tag)
            true
        }
    }

}
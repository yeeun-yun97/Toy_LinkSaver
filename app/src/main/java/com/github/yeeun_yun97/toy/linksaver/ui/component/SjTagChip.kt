package com.github.yeeun_yun97.toy.linksaver.ui.component

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import com.github.yeeun_yun97.clone.ynmodule.ui.component.CustomComponentStyleUtil
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.google.android.material.chip.Chip

class SjTagChip
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    val tag: SjTag
) : Chip(context, attrs, defStyleAttr) {

    init {
        setText(tag.name)
        setCheckableMode()
        isCheckedIconVisible = false

        CustomComponentStyleUtil.setMaterialCustomChipStyle(this)
    }

    fun setCheckableMode() {
        isCheckable = true
        id = tag.tid
    }

    fun setViewMode() {
        isClickable = false
        isChecked = true
        isCheckable = false
    }

    fun setCheckableAndLongClickableMode(
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
package com.github.yeeun_yun97.toy.linksaver.ui.component.customView

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import com.github.yeeun_yun97.clone.ynmodule.ui.component.CustomComponentStyleUtil
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.google.android.material.chip.Chip

data class TagValue(
    val tag: SjTag,
    val groupName: String? = null
)

class SjTagChip : Chip {
    private lateinit var tagValue: TagValue
    val tid get() = tagValue.tag.tid
    val tag get() = tagValue.tag

    constructor(context: Context?)
            : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?)
            : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        // apply style
        CustomComponentStyleUtil.setMaterialCustomChipStyle(this)
    }

    fun setTagValue(tagValue: TagValue) {
        this.tagValue = tagValue
        val chipName =
            if (!tagValue.groupName.isNullOrEmpty()) {
                "${tagValue.groupName}: ${tagValue.tag.name}"
            } else {
                tagValue.tag.name
            }
        setText(chipName)
    }

    // no interaction chip Mode

    fun setViewMode() {
        Log.d("setViewModeChip", "viewMode on")
        isCheckedIconVisible = false
        isCheckable = true
        isChecked = true
        isCheckable = false
        isClickable = false
    }

    // only checkable chip Mode
    fun setCheckableMode(
        onCheckedChangeListener: OnCheckedChangeListener, checked : Boolean
    ) {
        isCheckedIconVisible = false
        isCheckable = true
        isChecked = checked
        id = tagValue.tag.tid
        setOnCheckedChangeListener(onCheckedChangeListener)
    }

    // deletable and long-clickable and deletable Chip Mode
    fun setDeletableAndLongClickableMode(
        deleteOperation: (SjTag) -> Unit,
        longClickOperation: (SjTag) -> Unit
    ) {
        isCheckable = false
        isCloseIconVisible = true
        setOnCloseIconClickListener { if (it is SjTagChip) deleteOperation(it.tag) }
        setOnLongClickListener { if (it is SjTagChip) longClickOperation(it.tag); true }
    }


}
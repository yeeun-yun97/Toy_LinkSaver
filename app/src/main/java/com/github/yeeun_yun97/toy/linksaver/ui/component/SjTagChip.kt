package com.github.yeeun_yun97.toy.linksaver.ui.component

import android.content.Context
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.google.android.material.chip.Chip

class SjTagChip(context: Context, val tag: SjTag) : Chip(context) {

    init{
        setText(tag.name)
        isCheckable=true
        id=tag.tid
    }

}
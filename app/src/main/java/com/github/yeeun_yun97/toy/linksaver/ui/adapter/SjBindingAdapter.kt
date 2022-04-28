package com.github.yeeun_yun97.toy.linksaver.ui.adapter

import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.ui.component.SjTagChip
import com.google.android.material.chip.ChipGroup

class SjBindingAdapter {
    companion object{
        @JvmStatic
        @BindingAdapter("chipCountData")
        fun setTextByCount(view: TextView, count:Int){
            if(count<2){
                view.isVisible= false
            }else{
                view.isVisible=true
                view.setText("및 ${count-1}개")
            }
        }

        @JvmStatic
        @BindingAdapter("chipDataList")
        fun setChipByList(view: ChipGroup, tags:List<SjTag>){
            view.removeAllViews()
            if(tags.size>0){
                val chip = SjTagChip(view.context,tags[0])
                chip.isCheckable=false
                chip.isClickable=false
                view.addView(chip)
            }
        }
    }
}
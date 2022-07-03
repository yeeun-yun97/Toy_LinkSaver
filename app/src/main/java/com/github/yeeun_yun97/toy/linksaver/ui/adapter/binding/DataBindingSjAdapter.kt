package com.github.yeeun_yun97.toy.linksaver.ui.adapter.binding

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.github.yeeun_yun97.toy.linksaver.data.model.LinkDetailValue
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.ui.component.SjPreview
import com.github.yeeun_yun97.toy.linksaver.ui.component.SjTagChip
import com.github.yeeun_yun97.toy.linksaver.ui.component.TagValue
import com.google.android.material.chip.ChipGroup

class DataBindingSjAdapter {
    companion object {
        @JvmStatic
        @BindingAdapter("chipDataList")
        fun setChipByList(view: ChipGroup, tags: List<SjTag>?) {
            view.removeAllViews()
            if (!tags.isNullOrEmpty()) {
                for (tag in tags) {
                    val chip = SjTagChip(view.context)
                    chip.setTagValue(TagValue(tag))
                    chip.setViewMode()
                    view.addView(chip)
                }
            }
        }

        @JvmStatic
        @BindingAdapter("visibilityByList")
        fun setVisibilityByList(view: View, list: List<Any?>?) {
            view.visibility =
                when (list.isNullOrEmpty()) {
                    true -> View.GONE
                    false -> View.VISIBLE
                }
        }

        @JvmStatic
        @BindingAdapter("listEmptyView")
        fun setEmptyViewByList(view: View, list: List<Any?>?) {
            view.visibility =
                when (list.isNullOrEmpty()) {
                    true -> View.VISIBLE
                    false -> View.GONE
                }
        }

        @JvmStatic
        @BindingAdapter("previewContent")
        fun setPreviewByLinkDetailValue(view: SjPreview, linkDetailValue: LinkDetailValue?) {
            if (linkDetailValue != null)
                view.setPreview(
                    linkDetailValue.isVideo,
                    linkDetailValue.url,
                    linkDetailValue.preview
                )
        }

        @JvmStatic
        @BindingAdapter("textOrHide")
        fun setTextOrHide(view: TextView, string: String) {
            if (string.isEmpty()) {
                view.visibility = View.GONE
            } else {
                view.setText(string)
                view.visibility = View.VISIBLE
            }
        }
    }


}
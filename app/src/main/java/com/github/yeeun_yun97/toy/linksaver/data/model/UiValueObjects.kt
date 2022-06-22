package com.github.yeeun_yun97.toy.linksaver.data.model

import com.github.yeeun_yun97.toy.linksaver.ui.component.SjUtil

data class SettingItemValue(
    val name: String,
    val open: () -> Unit
)

data class LinkDetailValue(
    val lid: Int,
    val name: String,
    val fullUrl: String,
    val preview: String,
    val isVideo: Boolean,
    val isYoutubeVideo: Boolean,
    val tags: List<SjTag>
)

data class VideoData(
    val lid: Int = 0,
    val url: String,
    val name: String,
    val thumbnail: String,
    val isYoutubeVideo: Boolean,
    val tagList: List<SjTag>
) {
    companion object {
        fun getVideoDataBy(link: SjLinksAndDomainsWithTags): VideoData {
            val fullUrl = LinkModelUtil.getFullUrl(link)
            return VideoData(
                link.link.lid,
                fullUrl,
                link.link.name,
                link.link.preview,
                SjUtil.checkYoutubePrefix(fullUrl),
                link.tags
            )
        }
    }
}


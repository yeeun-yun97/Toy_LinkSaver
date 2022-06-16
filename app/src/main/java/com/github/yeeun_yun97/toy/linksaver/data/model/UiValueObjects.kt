package com.github.yeeun_yun97.toy.linksaver.data.model

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
)


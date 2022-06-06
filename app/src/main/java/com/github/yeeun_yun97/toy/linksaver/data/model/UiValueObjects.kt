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

data class FullNameTagValue(
    val tag: SjTag,
    val fullName: String
)


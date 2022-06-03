package com.github.yeeun_yun97.toy.linksaver.data.model

data class SettingData(val name: String, val open: () -> Unit)

data class LinkDetailData(
    val fullUrl:String,
    val link:SjLink,
    val tags:List<SjTag>,
    val isVideo:Boolean
)
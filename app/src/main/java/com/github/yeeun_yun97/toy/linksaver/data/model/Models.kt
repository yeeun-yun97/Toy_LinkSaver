package com.github.yeeun_yun97.toy.linksaver.data.model

data class SjDomain(
    var name: String,
    var url: String
)

data class SjLink(
    var name: String,
    val domain: SjDomain,
    var url: String,
    val tags: SjTag,
) { val fullUrl: String get() = "${domain.url}${url}" }

data class SjTag(var name: String)

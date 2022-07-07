package com.github.yeeun_yun97.toy.linksaver.data.model

data class SjDomainForShare(
    val did :Int,
    val name: String,
    val url:String,
    val tidList:List<Int>
)

data class SjLinkForShare(
    val lid: Int,
    val name: String,
    val did:Int,
    val url:String,
    val icon:String,
    val preview:String,
    val type:ELinkType,
    val tidList: List<Int>
)

data class SjShare(
    val groups:List<SjTagGroup>,
    val tags : List<SjTag>,
    val domains: List<SjDomainForShare>,
    val links : List<SjLinkForShare>
)
package com.github.yeeun_yun97.toy.linksaver.data.model

import androidx.room.*

@Entity
data class SjDomain(
    @PrimaryKey val did: Int,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "url") var url: String
)

@Entity
data class SjLink(
    @PrimaryKey val lid: Int,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "did") val did: Int,
    @ColumnInfo(name = "url") var url: String,
)

@Entity
data class SjTag(
    @PrimaryKey val tid: Int,
    @ColumnInfo(name = "name") var name: String
)

//1 by n relation ::
//하나의 도메인은 여러 개의 링크를 가진다.
//하나의 링크는 하나의 도메인을 가진다.
data class SjDomainWithLinks(
    @Embedded val domain: SjDomain,
    @Relation(
        parentColumn = "did",
        entityColumn = "did"
    )
    val links: List<SjLink>
)


//n by m relation ::
//하나의 링크는 여러 개의 태그를 가진다.
//하나의 태그는 여러 개의 링크를 가진다.
@Entity(primaryKeys = ["tid", "lid"])
data class LinkTagCrossRef(
    val tid: Int,
    val lid: Int
)

data class SjLinkWithTags(
    @Embedded val link: SjLink,
    @Relation(
        parentColumn = "lid",
        entityColumn = "tid",
        associateBy = Junction(LinkTagCrossRef::class)
    ) val tags: List<SjTag>
)

data class SjTagWithLinks(
    @Embedded val tag: SjTag,
    @Relation(
        parentColumn = "tid",
        entityColumn = "lid",
        associateBy = Junction(LinkTagCrossRef::class)
    ) val links: List<SjLink>
)

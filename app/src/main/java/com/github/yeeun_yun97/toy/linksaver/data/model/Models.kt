package com.github.yeeun_yun97.toy.linksaver.data.model

import androidx.room.*

data class NameOnly(
    @ColumnInfo(name = "name") var name: String
)

@Entity
data class SjDomain(
    @PrimaryKey(autoGenerate = true) val did: Int=0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "url") var url: String
)

@Entity
data class SjLink(
    @PrimaryKey(autoGenerate = true) val lid: Int=0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "did") val did: Int,
    @ColumnInfo(name = "url") var url: String,
)

@Entity
data class SjTag(
    @PrimaryKey(autoGenerate = true) val tid: Int=0,
    @ColumnInfo(name = "name") var name: String
)

/*
data class SjLinkWithTagsAndDomain(
    @Embedded val domain: SjDomain,
    @Relation(
        parentColumn = "lid",
        entityColumn = "tid",
        associateBy = Junction(LinkTagCrossRef::class)
    ) val tags: List<SjTag>,
    @Relation(
        parentColumn = "did",
        entityColumn = "did"
    ) val link: SjLink
)*/


//1 by n relation ::
//하나의 도메인은 여러 개의 링크를 가진다.
//하나의 링크는 하나의 도메인을 가진다.
data class SjLinkAndDomain(
    @Embedded val link: SjLink,
    @Relation(
        parentColumn = "did",
        entityColumn = "did"
    )
    val domain: SjDomain
)

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
    val lid: Int,
    val tid: Int
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

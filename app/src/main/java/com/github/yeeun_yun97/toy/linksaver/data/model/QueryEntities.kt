package com.github.yeeun_yun97.toy.linksaver.data.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class SjLinksAndDomainsWithTags(
    @Embedded val link: SjLink,
    @Relation(
        parentColumn = "did",
        entityColumn = "did"
    ) val domain: SjDomain,
    @Relation(
        parentColumn = "lid",
        entityColumn = "tid",
        associateBy = Junction(LinkTagCrossRef::class)
    ) val tags: List<SjTag>
)

data class SjTagGroupWithTags(
    @Embedded val tagGroup: SjTagGroup,
    @Relation(
        parentColumn = "gid",
        entityColumn = "gid",
    ) val tags: List<SjTag>
)

// n by m relation :: search and tag
data class SjSearchWithTags(
    @Embedded val search: SjSearch,
    @Relation(
        parentColumn = "sid",
        entityColumn = "tid",
        associateBy = Junction(SearchTagCrossRef::class)
    ) val tags: List<SjTag>
)


data class SjDomainWithTags(
    @Embedded val domain: SjDomain,
    @Relation(
        parentColumn = "did",
        entityColumn = "tid",
        associateBy = Junction(DomainTagCrossRef::class)
    )
    val tags: List<SjTag>
)

data class SjLinkWithTags(
    @Embedded val link: SjLink,
    @Relation(
        parentColumn = "lid",
        entityColumn = "tid",
        associateBy = Junction(LinkTagCrossRef::class)
    )
    val tags: List<SjTag>
)

/*
// 1 by n relation :: domain and link
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
 */
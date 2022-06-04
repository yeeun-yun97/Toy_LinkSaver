package com.github.yeeun_yun97.toy.linksaver.data.model

import androidx.room.*

enum class ELinkType() {
    video, link;
}

class Converters {
    @TypeConverter
    fun toELinkType(value: String) = enumValueOf<ELinkType>(value)

    @TypeConverter
    fun fromELinkType(value: ELinkType) = value.name
}


// entities
@Entity(
    indices = [
        Index(value = ["name"]),
        Index(value = ["url"], unique = true),
    ]
)
data class SjDomain(
    @PrimaryKey(autoGenerate = true) val did: Int = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "url") var url: String
)

@Entity(
    indices = [
        Index(value = ["name"]),
        Index(value = ["did"]),
        Index(value = ["type"])
    ]
)
data class SjLink(
    @PrimaryKey(autoGenerate = true) val lid: Int = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "did") var did: Int,
    @ColumnInfo(name = "url") var url: String,

    @ColumnInfo(name = "icon", defaultValue = "") var icon: String = "",
    @ColumnInfo(name = "preview", defaultValue = "") var preview: String = "",
    @ColumnInfo(name = "type", defaultValue = "link")
    @TypeConverters(Converters::class)
    var type: ELinkType = ELinkType.link,
)

@Entity(indices = [Index(value = ["name"], unique = true)])
data class SjTagGroup(
    @PrimaryKey(autoGenerate = true) val gid: Int = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "is_private") var isPrivate: Boolean,
)

@Entity(indices = [Index(value = ["name"], unique = true)])
data class SjTag(
    @PrimaryKey(autoGenerate = true) val tid: Int = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "gid") var gid: Int = 1
)

@Entity(indices = [Index(value = ["keyword"])])
data class SjSearch(
    @PrimaryKey(autoGenerate = true) val sid: Int = 0,
    @ColumnInfo(name = "keyword") var keyword: String
)


// entities :: mapping table
@Entity(primaryKeys = ["sid", "tid"])
data class SearchTagCrossRef(
    val sid: Int,
    val tid: Int
)

@Entity(primaryKeys = ["lid", "tid"])
data class LinkTagCrossRef(
    val lid: Int,
    val tid: Int
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


//n by m relation :: link and tag
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


// 1 by n by m relation :: domain and link and tag
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

// 1 by n relation :: tagGroup and tag
data class SjTagGroupWithTags(
    @Embedded val tagGroup: SjTagGroup,
    @Relation(
        parentColumn = "gid",
        entityColumn = "gid",
    ) var tags: List<SjTag>
)

class LinkModelUtil{
    companion object {
        fun getFullUrl(data: SjLinksAndDomainsWithTags): String {
            val domainUrl = data.domain.url
            val linkUrl = data.link.url
            return StringBuilder(domainUrl.length + linkUrl.length)
                .append(domainUrl)
                .append(linkUrl)
                .toString()
        }
    }
}


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

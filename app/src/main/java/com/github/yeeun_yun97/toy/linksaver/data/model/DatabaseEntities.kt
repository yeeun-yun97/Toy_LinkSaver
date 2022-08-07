package com.github.yeeun_yun97.toy.linksaver.data.model

import androidx.room.*

@Entity(
    indices = [
        Index(value = ["name"]),
        Index(value = ["url"], unique = true),
    ]
)
data class SjDomain(
    @PrimaryKey(autoGenerate = true) val did: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "url") val url: String
)

@Entity(primaryKeys = ["did", "tid"])
data class DomainTagCrossRef(
    val did: Int,
    val tid: Int
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
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "did") val did: Int,
    @ColumnInfo(name = "url") val url: String,

    @ColumnInfo(name = "icon", defaultValue = "") val icon: String = "",
    @ColumnInfo(name = "preview", defaultValue = "") val preview: String = "",
    @ColumnInfo(name = "type", defaultValue = "link")
    @TypeConverters(Converters::class)
    val type: ELinkType = ELinkType.link,
)

@Entity(indices = [Index(value = ["name"], unique = true)])
data class SjTagGroup(
    @PrimaryKey(autoGenerate = true) val gid: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "is_private") val isPrivate: Boolean,
)

@Entity(indices = [Index(value = ["name"], unique = true)])
data class SjTag(
    @PrimaryKey(autoGenerate = true) val tid: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "gid") val gid: Int = 1
)

@Entity(indices = [Index(value = ["keyword"])])
data class SjSearch(
    @PrimaryKey(autoGenerate = true) val sid: Int = 0,
    @ColumnInfo(name = "keyword") val keyword: String
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


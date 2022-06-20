package com.github.yeeun_yun97.toy.linksaver.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.github.yeeun_yun97.toy.linksaver.data.model.*

@Dao
interface SjLinkDao {
    // select data list
    @Transaction
    @Query("SELECT * FROM SjLink ORDER BY lid DESC")
    suspend fun selectAllLinks(): List<SjLinksAndDomainsWithTags>

    @Transaction
    @Query("SELECT * FROM SjLink WHERE lid NOT IN (SELECT ref.lid FROM LinkTagCrossRef as ref WHERE ref.tid NOT IN (SELECT tag.tid FROM SjTag as tag WHERE tag.gid NOT IN (SELECT g.gid FROM SjTagGroup as g WHERE is_private = 1))) ORDER BY lid DESC")
    suspend fun selectLinksPublic(): List<SjLinksAndDomainsWithTags>

    @Transaction
    @Query(
        "SELECT link.lid, link.name, link.did, link.url, link.icon, link.preview, link.type FROM SjLink as link "
                + "INNER JOIN linkTagCrossRef as ref ON link.lid = ref.lid "
                + "INNER JOIN SjTag as tag ON ref.tid = tag.tid "
                + "WHERE link.name LIKE :keyword "
                + "AND tag.tid IN(:tags) "
                + "GROUP BY link.lid " //prevent duplicates
                + "HAVING count(*) == :size "
                + "ORDER BY link.lid desc"
    )
    suspend fun selectLinksByNameAndTags(
        keyword: String,
        tags: List<Int>,
        size: Int
    ): List<SjLinksAndDomainsWithTags>

    @Transaction
    @Query(
        "SELECT link.lid, link.name, link.did, link.url, link.icon, link.preview, link.type FROM SjLink as link "
                + "INNER JOIN linkTagCrossRef as ref ON link.lid = ref.lid "
                + "INNER JOIN SjTag as tag ON ref.tid = tag.tid "
                + "WHERE link.name LIKE :keyword "
                + "AND tag.tid IN(:tags) " +
                "AND link.lid NOT IN(SELECT ref.lid FROM LinkTagCrossRef as ref WHERE ref.tid NOT IN (SELECT tag.tid FROM SjTag as tag WHERE tag.gid NOT IN (SELECT g.gid FROM SjTagGroup as g WHERE is_private = 1))) "
                + "GROUP BY link.lid " //prevent duplicates
                + "HAVING count(*) == :size "
                + "ORDER BY link.lid desc"
    )
    suspend fun selectLinksByNameAndTagsPublic(
        keyword: String,
        tags: List<Int>,
        size: Int
    ): List<SjLinksAndDomainsWithTags>

    @Transaction
    @Query(
        "SELECT * FROM SjLink " +
                "WHERE name LIKE :keyword " +
                "AND lid NOT IN(SELECT ref.lid FROM LinkTagCrossRef as ref WHERE ref.tid NOT IN (SELECT tag.tid FROM SjTag as tag WHERE tag.gid NOT IN (SELECT g.gid FROM SjTagGroup as g WHERE is_private = 1))) " +
                "ORDER BY lid desc"
    )
    suspend fun selectLinksByNamePublic(
        keyword: String
    ): List<SjLinksAndDomainsWithTags>

    @Transaction
    @Query("SELECT * FROM SjLink WHERE name LIKE :keyword ORDER BY lid desc")
    suspend fun selectLinksByName(
        keyword: String
    ): List<SjLinksAndDomainsWithTags>


    // XXX not yet (get video type)
    @Transaction
    @Query("SELECT * FROM SjLink WHERE Type = :type ORDER BY lid desc")
    fun getAllLinksByType(type: String): LiveData<List<SjLinksAndDomainsWithTags>>
    @Transaction
    @Query("SELECT * FROM SjLink WHERE Type = :type AND lid not in (SELECT ref.lid FROM LinkTagCrossRef as ref WHERE ref.tid NOT IN (SELECT tag.tid FROM SjTag as tag WHERE tag.gid NOT IN (SELECT g.gid FROM SjTagGroup as g WHERE is_private = 1))) ORDER BY lid desc")
    fun getPublicLinksByType(type: String): LiveData<List<SjLinksAndDomainsWithTags>>


    // select single
    @Transaction
    @Query("SELECT * FROM SjLink WHERE lid = :lid")
    suspend fun selectLinkByLid(lid: Int): SjLinksAndDomainsWithTags


    // insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLinkTagCrossRef(newCrossRef: LinkTagCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLinkTagCrossRefs(vararg newCrossRef: LinkTagCrossRef): List<Long>

    @Insert
    suspend fun insertLink(newLink: SjLink): Long


    // update
    @Update
    suspend fun updateLink(link: SjLink)
    @Update
    suspend fun updateLinks(vararg links: SjLink)


    // delete
    @Query("DELETE FROM LinkTagCrossRef WHERE lid= :lid")
    suspend fun deleteLinkTagCrossRefsByLid(lid: Int)

    @Query("DELETE FROM LinkTagCrossRef WHERE lid = :lid AND tid IN(:tids)")
    suspend fun deleteLinkTagCrossRefsByLidAndTids(lid: Int, tids: List<Int>)

    @Query("DELETE FROM SjLink WHERE lid = :lid")
    suspend fun deleteLinkByLid(lid: Int)


}
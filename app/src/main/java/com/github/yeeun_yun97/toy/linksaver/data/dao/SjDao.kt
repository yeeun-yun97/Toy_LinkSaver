package com.github.yeeun_yun97.toy.linksaver.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.github.yeeun_yun97.toy.linksaver.data.model.*

@Dao
interface SjDao {

    // get All Entities from Database
    @Query("SELECT * FROM SjDomain WHERE did NOT IN (1)")
    fun getAllDomainsExceptDefault(): LiveData<List<SjDomain>>

    @Query("SELECT * FROM SjDomain")
    fun getAllDomains(): LiveData<List<SjDomain>>

    @Query("SELECT * FROM SjTag ORDER BY name")
    fun getAllTags(): LiveData<List<SjTag>>

    @Query("SELECT * FROM SjTagGroup ORDER BY gid")
    fun getAllTagGroups(): LiveData<List<SjTagGroup>>



    @Transaction
    @Query("SELECT * FROM SjLink ORDER BY lid DESC")
    fun getAllLinksAndDomainsWithTags()
            : LiveData<List<SjLinksAndDomainsWithTags>>

    @Transaction
    @Query("SELECT * FROM SjLink WHERE lid NOT IN (SELECT ref.lid FROM LinkTagCrossRef as ref WHERE ref.tid NOT IN (SELECT tag.tid FROM SjTag as tag WHERE tag.gid NOT IN (SELECT g.gid FROM SjTagGroup as g WHERE is_private = 1))) ORDER BY lid DESC")
    fun getPublicLinksAndDomainsWithTags()
            : LiveData<List<SjLinksAndDomainsWithTags>>

    @Transaction
    @Query("SELECT * FROM SjTagGroup WHERE gid != 1 ORDER BY name")
    fun getTagGroupsWithTags()
            : LiveData<List<SjTagGroupWithTags>>

    @Transaction
    @Query("SELECT * FROM SjTagGroup WHERE gid != 1 AND is_private = 0 ORDER BY name")
    fun getTagGroupsWithTagsNotPrivate(): LiveData<List<SjTagGroupWithTags>>

    @Transaction
    @Query("SELECT * FROM SjTagGroup ORDER BY name")
    fun getAllTagGroupsWithTags()
            : LiveData<List<SjTagGroupWithTags>>

    @Transaction
    @Query("SELECT * FROM SjTagGroup WHERE is_private=0 ORDER BY gid")
    fun getNotPrivateTagGroupsWithTags()
            : LiveData<List<SjTagGroupWithTags>>

    @Query("SELECT COUNT(*) FROM SjTagGroup")
    suspend fun getTagGroupCount(): Int

    @Query("SELECT COUNT(*) FROM SjDomain")
    suspend fun getDomainCount(): Int

    @Query("SELECT COUNT(*) FROM SjLink")
    suspend fun getLinkCount(): Int

    @Query("SELECT COUNT(*) FROM SjTag")
    suspend fun getTagCount(): Int

    @Query("SELECT COUNT(*) FROM LinkTagCrossRef")
    suspend fun getLinkTagCrossRefCount(): Int


    // search link query by link name and tags
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
    suspend fun searchLinksAndDomainsWithTagsByLinkNameAndTags(
        keyword: String, tags: List<Int>, size: Int
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
    suspend fun searchPublicLinksAndDomainsWithTagsByLinkNameAndTags(
        keyword: String, tags: List<Int>, size: Int
    ): List<SjLinksAndDomainsWithTags>

    // search link query by link name
    @Transaction
    @Query("SELECT * FROM SjLink WHERE name LIKE :keyword ORDER BY lid desc")
    suspend fun searchLinksAndDomainsWithTagsByLinkName(
        keyword: String
    ): List<SjLinksAndDomainsWithTags>

    @Transaction
    @Query("SELECT * FROM SjLink WHERE name LIKE :keyword AND lid NOT IN(SELECT ref.lid FROM LinkTagCrossRef as ref WHERE ref.tid NOT IN (SELECT tag.tid FROM SjTag as tag WHERE tag.gid NOT IN (SELECT g.gid FROM SjTagGroup as g WHERE is_private = 1))) ORDER BY lid desc")
    suspend fun searchPublicLinksAndDomainsWithTagsByLinkName(
        keyword: String
    ): List<SjLinksAndDomainsWithTags>


    // Insert queries
    @Insert
    suspend fun insertDomain(newDomain: SjDomain): Long

    @Insert
    suspend fun insertLink(newLink: SjLink): Long



    @Insert
    suspend fun insertTag(newTag: SjTag): Long

    @Insert
    suspend fun insertTagGroup(newTagGroup: SjTagGroup): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLinkTagCrossRef(newCrossRef: LinkTagCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLinkTagCrossRefs(vararg newCrossRef: LinkTagCrossRef): List<Long>




    // update queries
    @Update
    suspend fun updateLink(link: SjLink)

    @Update
    suspend fun updateTag(tag: SjTag)

    @Update
    suspend fun updateTags(vararg tags: SjTag)

    @Update
    suspend fun updateTagGroup(tagGroup: SjTagGroup)

    @Update
    suspend fun updateDomain(domain: SjDomain)

    @Update
    suspend fun updateLinks(vararg links: SjLink)

    @Query("UPDATE SjTag SET gid = 1 WHERE gid = :gid")
    suspend fun updateTagToBasicGroupByGid(gid: Int)


    // delete queries
    @Delete
    suspend fun deleteDomain(newDomain: SjDomain)

    @Delete
    suspend fun deleteLink(newLink: SjLink)

    @Delete
    suspend fun deleteTag(newTag: SjTag)

    @Delete
    suspend fun deleteTags(vararg tag: SjTag)

    @Delete
    suspend fun deleteLinkTagCrossRefs(vararg ref: LinkTagCrossRef)



    @Query("DELETE FROM SjLink WHERE did = :did")
    suspend fun deleteLinksByDid(did: Int)

    @Query("DELETE FROM LinkTagCrossRef WHERE lid= :lid")
    suspend fun deleteLinkTagCrossRefsByLid(lid: Int)

    @Query("DELETE FROM LinkTagCrossRef WHERE tid= :tid")
    suspend fun deleteLinkTagCrossRefsByTid(tid: Int)


    @Query("DELETE FROM LinkTagCrossRef WHERE lid = :lid AND tid IN(:tids)")
    suspend fun deleteLinkTagCrossRefsByLidAndTid(lid: Int, tids: MutableList<Int>)

    @Query("DELETE FROM SjLink WHERE lid = :lid")
    suspend fun deleteLinkByLid(lid: Int)

    @Query("DELETE FROM SjTagGroup WHERE gid = :gid")
    suspend fun deleteTagGroupByGid(gid: Int)


    // query by key
    @Transaction
    @Query("SELECT * FROM SjLink WHERE lid = :lid")
    suspend fun getLinkAndDomainWithTagsByLid(lid: Int): SjLinksAndDomainsWithTags

    @Transaction
    @Query("SELECT * FROM SjLink WHERE did = :did")
    fun getLinkAndDomainWithTagsByDid(did: Int): List<SjLinksAndDomainsWithTags>

    @Query("SELECT * FROM SjTag WHERE tid = :tid")
    suspend fun getTagByTid(tid: Int): SjTag

    @Query("SELECT * FROM SjDomain WHERE did = :did")
    suspend fun getDomainByDid(did: Int): SjDomain

    @Transaction
    @Query("SELECT * FROM SjLink WHERE Type = :type ORDER BY lid desc")
    fun getAllLinksByType(type: String): LiveData<List<SjLinksAndDomainsWithTags>>

    @Transaction
    @Query("SELECT * FROM SjLink WHERE Type = :type AND lid not in (SELECT ref.lid FROM LinkTagCrossRef as ref WHERE ref.tid NOT IN (SELECT tag.tid FROM SjTag as tag WHERE tag.gid NOT IN (SELECT g.gid FROM SjTagGroup as g WHERE is_private = 1))) ORDER BY lid desc")
    fun getPublicLinksByType(type: String): LiveData<List<SjLinksAndDomainsWithTags>>

    @Query("SELECT * FROM SjTag WHERE gid = :gid ORDER BY name")
    fun getAllTagsByGid(gid: Int): LiveData<List<SjTag>>

    @Transaction
    @Query("SELECT * FROM SjTagGroup WHERE gid= :gid")
    fun getTagGroupWithTagsByGid(gid: Int): SjTagGroupWithTags

    @Transaction
    @Query("SELECT * FROM SjTagGroup WHERE gid= 1")
    fun getBasicTagGroupWithTags(): LiveData<SjTagGroupWithTags>


}
package com.github.yeeun_yun97.toy.linksaver.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.github.yeeun_yun97.toy.linksaver.data.model.*

@Dao
interface SjDao {
    // get All Entities from Database
    @Query("SELECT * FROM SjDomain")
    fun getAllDomains(): LiveData<List<SjDomain>>

    @Query("SELECT * FROM SjTag")
    fun getAllTags(): LiveData<List<SjTag>>

    @Transaction
    @Query("SELECT * FROM SjSearch ORDER BY sid DESC")
    fun getAllSearch(): LiveData<List<SjSearchWithTags>>

    @Transaction
    @Query("SELECT * FROM SjLink ORDER BY lid DESC")
    fun getAllLinksAndDomainsWithTags()
            : LiveData<List<SjLinksAndDomainsWithTags>>

    @Query("SELECT COUNT(*) FROM SjDomain")
    suspend fun getDomainCount(): Int


    // search link query by link name and tags
    @Transaction
    @Query(
        "SELECT link.lid, link.name, link.did, link.url FROM SjLink as link "
                + "INNER JOIN linkTagCrossRef as ref ON link.lid = ref.lid "
                + "INNER JOIN SjTag as tag ON ref.tid = tag.tid "
                + "WHERE link.name LIKE :keyword "
                + "AND tag.tid IN(:tags)"
                + "GROUP BY link.lid" //prevent duplicates
    )
    suspend fun searchLinksAndDomainsWithTagsByLinkNameAndTags(
        keyword: String, tags: List<Int>
    ): List<SjLinksAndDomainsWithTags>


    // Insert queries
    @Insert
    suspend fun insertDomain(newDomain: SjDomain): Long

    @Insert
    suspend fun insertLink(newLink: SjLink): Long

    @Insert
    suspend fun insertSearch(newSearch: SjSearch): Long

    @Insert
    suspend fun insertTag(newTag: SjTag): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLinkTagCrossRef(newCrossRef: LinkTagCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLinkTagCrossRefs(vararg newCrossRef: LinkTagCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchTagCrossRefs(vararg ref: SearchTagCrossRef)


    // update queries
    @Update
    suspend fun updateLink(link: SjLink)

    @Update
    suspend fun updateTag(tag: SjTag)

    @Update
    suspend fun updateDomain(domain: SjDomain)


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

    @Query("Delete FROM SearchTagCrossRef")
    suspend fun deleteAllSearchTagCrossRefs()

    @Query("Delete FROM SjSearch")
    suspend fun deleteAllSearch()

    @Query("DELETE FROM SjLink WHERE did = :did")
    suspend fun deleteLinksByDid(did: Int)

    @Query("DELETE FROM LinkTagCrossRef WHERE lid= :lid")
    suspend fun deleteLinkTagCrossRefsByLid(lid: Int)

    @Query("DELETE FROM LinkTagCrossRef WHERE tid= :tid")
    suspend fun deleteLinkTagCrossRefsByTid(tid: Int)


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

}
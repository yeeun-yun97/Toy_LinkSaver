package com.github.yeeun_yun97.toy.linksaver.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.github.yeeun_yun97.toy.linksaver.data.model.*

@Dao
interface SjDao {

    //////Test Passed Methods///////
    @Query("SELECT * FROM SjDomain")
    fun getAllDomains(): LiveData<List<SjDomain>>

    @Query("SELECT * FROM SjTag")
    fun getAllTags(): LiveData<List<SjTag>>

    @Query("SELECT name FROM SjDomain")
    fun getAllDomainNames(): LiveData<List<String>>

    @Transaction
    @Query("SELECT * FROM SjLink WHERE name LIKE :linkName")
    fun searchLinksAndDomainsWithTagsByLinkName(linkName: String)
            : List<SjLinksAndDomainsWithTags>

    @Transaction
    @Query("SELECT * FROM SjSearch ORDER BY sid DESC")
    fun getAllSearch()
            : LiveData<List<SjSearchWithTags>>

    @Transaction
    @Query("SELECT * FROM SjLink ORDER BY lid DESC")
    fun getAllLinksAndDomainsWithTags()
            : LiveData<List<SjLinksAndDomainsWithTags>>


    //insert suspend functions
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDomain(newDomain: SjDomain)

    @Insert
    suspend fun insertLink(newLink: SjLink): Long

    @Insert
    suspend fun insertSearch(newSearch: SjSearch): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(newTag: SjTag)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLinkTagCrossRef(newCrossRef: LinkTagCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLinkTagCrossRefs(vararg newCrossRef: LinkTagCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchTagCrossRefs(vararg ref: SearchTagCrossRef)


    @Update
    fun updateLink(link: SjLink)

    @Update
    fun updateTag(tag: SjTag)

    @Update
    fun updateDomain(domain: SjDomain)


    /////NOT YET TESTED//////

    @Delete
    fun deleteDomain(newDomain: SjDomain)

    @Delete
    fun deleteLink(newLink: SjLink)

    @Delete
    fun deleteTag(newTag: SjTag)

    @Delete
    suspend fun deleteTags(vararg tag: SjTag)

    @Delete
    suspend fun deleteLinkTagCrossRefs(vararg ref: LinkTagCrossRef)

    @Query("Delete FROM SearchTagCrossRef")
    fun deleteAllSearchTagCrossRefs()

    @Query("Delete FROM SjSearch")
    fun deleteAllSearch()

    @Transaction
    @Query("SELECT * FROM SjLink WHERE lid = :lid")
    fun getLinkAndDomainWithTagsByLid(lid: Int): SjLinksAndDomainsWithTags

    @Transaction
    @Query("SELECT * FROM SjLink WHERE did = :did")
    fun getLinkAndDomainWithTagsByDid(did: Int): List<SjLinksAndDomainsWithTags>

    @Query("DELETE FROM LinkTagCrossRef WHERE lid= :lid")
    fun deleteLinkTagCrossRefsByLid(lid: Int)

    @Query("DELETE FROM LinkTagCrossRef WHERE tid= :tid")
    fun deleteLinkTagCrossRefsByTid(tid: Int)

    @Query("SELECT * FROM SjTag WHERE tid = :tid")
    fun getTagByTid(tid: Int): SjTag

    @Query("DELETE FROM SjLink WHERE did = :did")
    fun deleteLinksByDid(did: Int)

    @Query("SELECT * FROM SjDomain WHERE did = :did")
    suspend fun getDomainByDid(did: Int): SjDomain


}
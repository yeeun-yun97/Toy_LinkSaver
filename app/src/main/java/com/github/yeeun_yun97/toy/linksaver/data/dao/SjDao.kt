package com.github.yeeun_yun97.toy.linksaver.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.github.yeeun_yun97.toy.linksaver.data.model.*

@Dao
interface SjDao {

    //////Test Passed Methods///////
    @Query("SELECT * FROM SjDomain")
    fun getAllDomains(): LiveData<List<SjDomain>>

    @Query("SELECT * FROM SjLink")
    fun getAllLinks(): LiveData<List<SjLink>>

    @Query("SELECT * FROM SjTag")
    fun getAllTags(): LiveData<List<SjTag>>

    @Query("SELECT name FROM SjDomain")
    fun getAllDomainNames(): LiveData<List<String>>

    @Transaction
    @Query("SELECT * FROM SjLink")
    fun getLinksWithTags(): List<SjLinkWithTags>

    @Transaction
    @Query("SELECT * FROM SjLink")
    fun getLinksAndDomain(): LiveData<List<SjLinkAndDomain>>

    @Query("SELECT Count(*) FROM LinkTagCrossRef WHERE lid=:lid")
    fun countLinkTagCrossRefByLid(lid:Int): Int

    //insert suspend functions
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDomain(newDomain: SjDomain)

    @Insert
    suspend fun insertLink(newLink: SjLink): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(newTag: SjTag)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLinkTagCrossRef(newCrossRef: LinkTagCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLinkTagCrossRefs(vararg newCrossRef: LinkTagCrossRef)


    /////NOT YET TESTED//////

//    @Transaction
//    @Query("SELECT * FROM SjLink")
//    fun getLinksWithTagsAndDomain():List<SjLinkWithTagsAndDomain>

    @Delete
    fun deleteDomain(newDomain: SjDomain)

    @Delete
    fun deleteLink(newLink: SjLink)

    @Delete
    fun deleteTag(newTag: SjTag)

}
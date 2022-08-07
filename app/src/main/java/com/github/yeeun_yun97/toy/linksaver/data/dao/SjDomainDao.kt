package com.github.yeeun_yun97.toy.linksaver.data.dao

import androidx.room.*
import com.github.yeeun_yun97.toy.linksaver.data.model.SjDomain
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLink
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLinksAndDomainsWithTags

@Dao
interface SjDomainDao {
    // select
    @Transaction
    @Query("SELECT * FROM SjLink WHERE did = :did")
    suspend fun selectLinksByDid(did: Int): List<SjLinksAndDomainsWithTags>

    @Query("SELECT * FROM SjDomain WHERE did NOT IN (1)")
    suspend fun selectDomainsNotDefault(): List<SjDomain>


    // select single
    @Query("SELECT * FROM SjDomain WHERE did = :did")
    suspend fun selectDomainByDid(did: Int): SjDomain


    // insert
    @Insert
    suspend fun insertDomain(newDomain: SjDomain): Long


    // update
    @Update
    suspend fun updateDomain(domain: SjDomain)

    @Update
    suspend fun updateLinks(vararg link: SjLink)

    @Query("UPDATE SjLink SET did=1 WHERE did = :did")
    suspend fun updateLinksToDefaultDomainByDid(did: Int)


    //delete
    @Delete
    suspend fun deleteDomain(newDomain: SjDomain)

    @Query("DELETE FROM SjLink WHERE did = :did")
    suspend fun deleteLinksByDid(did: Int)


}
package com.github.yeeun_yun97.toy.linksaver.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.github.yeeun_yun97.toy.linksaver.data.model.LinkTagCrossRef
import com.github.yeeun_yun97.toy.linksaver.data.model.SjDomain
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLink
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag

@Dao
interface SjDao {
    @Query("SELECT * FROM SjDomain")
    fun getDomains(): List<SjDomain>

    @Query("SELECT * FROM SjLink")
    fun getLinks(): List<SjLink>

    @Query("SELECT * FROM SjTag")
    fun getTags(): List<SjTag>

    @Insert
    fun insertDomain(newDomain: SjDomain)

    @Insert
    fun insertLinkTagCrossRef(newCrossRef: LinkTagCrossRef)

    @Insert
    fun insertLink(newLink:SjLink)

    @Insert
    fun insertTag(newTag:SjTag)

    @Delete
    fun deleteDomain(newDomain: SjDomain)

    @Delete
    fun deleteLink(newLink:SjLink)

    @Delete
    fun deleteTag(newTag:SjTag)

}
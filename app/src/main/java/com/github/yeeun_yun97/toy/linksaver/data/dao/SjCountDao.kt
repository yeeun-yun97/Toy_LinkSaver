package com.github.yeeun_yun97.toy.linksaver.data.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface SjCountDao {
    // count queries
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

    @Query("SELECT COUNT(*) FROM SearchTagCrossRef")
    suspend fun countAllSearchTagCrossRefs(): Int

    @Query("SELECT COUNT(*) FROM SjSearch")
    suspend fun getSearchSetCount(): Int


}
package com.github.yeeun_yun97.toy.linksaver.data.dao

import androidx.room.*
import com.github.yeeun_yun97.toy.linksaver.data.model.SearchTagCrossRef
import com.github.yeeun_yun97.toy.linksaver.data.model.SjSearch
import com.github.yeeun_yun97.toy.linksaver.data.model.SjSearchWithTags

@Dao
interface SjSearchSetDao {
    // select data list
    @Transaction
    @Query("SELECT * FROM SjSearch ORDER BY sid DESC")
    fun selectAllSearchSets(): List<SjSearchWithTags>

    @Transaction
    @Query("SELECT * FROM SjSearch " +
            "WHERE sid NOT IN (" +
                "SELECT sid FROM SearchTagCrossRef as ref " +
                "WHERE ref.tid NOT IN (" +
                    "SELECT tag.tid FROM SjTag as tag " +
                    "WHERE tag.gid NOT IN (" +
                        "SELECT g.gid FROM SjTagGroup as g " +
                        "WHERE is_private = 1" +
                    ")" +
                ")" +
            ") " +
            "ORDER BY sid DESC")
    fun selectSearchSetsPublic(): List<SjSearchWithTags>
    
    @Query(
        "SELECT search.sid FROM SjSearch as search "
                + "WHERE search.keyword = :keyword "
                + "AND search.sid NOT IN("
                + "SELECT ref.sid "
                + "FROM SearchTagCrossRef as ref "
                + "GROUP BY ref.sid"
                + ")"
    )
    suspend fun selectSearchSetByKeyword(
        keyword: String,
    ): List<Int>

    @Query(
        "SELECT search.sid FROM SjSearch as search "
                + "INNER JOIN SearchTagCrossRef as ref ON search.sid = ref.sid "
                + "INNER JOIN SjTag as tag ON ref.tid = tag.tid "
                + "WHERE search.keyword = :keyword "
                + "AND tag.tid IN(:tags) "
                + "AND search.sid NOT IN ("
                + "SELECT r.sid "
                + "FROM SearchTagCrossRef as r "
                + "WHERE r.tid NOT IN(:tags) "
                + "GROUP BY r.sid"
                + ")"
                + "GROUP BY search.sid "
                + "HAVING count(*) == :size"
    )
    suspend fun selectSearchSetByKeywordAndTags(
        keyword: String,
        tags: List<Int>,
        size: Int = tags.size
    ): List<Int>


    // insert
    @Insert
    suspend fun insertSearchSet(newSearch: SjSearch): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchTagCrossRefs(vararg ref: SearchTagCrossRef): List<Long>


    // delete
    @Query("Delete FROM SearchTagCrossRef")
    suspend fun deleteAllSearchTagCrossRefs()

    @Query("Delete FROM SjSearch")
    suspend fun deleteAllSearchSets()

    @Query("DELETE FROM SearchTagCrossRef WHERE sid IN (:sids)")
    suspend fun deleteSearchTagCrossRefsBySids(sids: List<Int>)

    @Query("DELETE FROM SjSearch WHERE sid IN(:sids)")
    suspend fun deleteSearchSetBySids(sids: List<Int>)


}
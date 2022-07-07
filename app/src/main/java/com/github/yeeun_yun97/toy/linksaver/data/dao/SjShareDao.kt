package com.github.yeeun_yun97.toy.linksaver.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.github.yeeun_yun97.toy.linksaver.data.model.SjDomainWithTags
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLinkWithTags
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTagGroup

@Dao
interface SjShareDao {
    @Query("SELECT * FROM SjTagGroup WHERE gid IS NOT 1")
    fun getTagGroupLiveData(): LiveData<List<SjTagGroup>>

    @Query("SELECT * FROM SjTag")
    fun getTagLiveData(): LiveData<List<SjTag>>

    @Transaction
    @Query("SELECT * FROM SjDomain WHERE did IS NOT 1")
    fun getDomainLiveData(): LiveData<List<SjDomainWithTags>>

    @Transaction
    @Query("SELECT * FROM SjLink")
    fun getLinkLiveData(): LiveData<List<SjLinkWithTags>>

}
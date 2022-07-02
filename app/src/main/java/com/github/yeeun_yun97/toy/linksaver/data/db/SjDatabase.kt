package com.github.yeeun_yun97.toy.linksaver.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.yeeun_yun97.toy.linksaver.data.dao.*
import com.github.yeeun_yun97.toy.linksaver.data.model.*

@Database(
    version = 4,
    entities = [
        SjTag::class,
        SjLink::class,
        LinkTagCrossRef::class,
        SjDomain::class,
        DomainTagCrossRef::class,
        SjSearch::class,
        SearchTagCrossRef::class,
        SjTagGroup::class,
    ]
)
abstract class SjDatabase : RoomDatabase() {
    abstract fun getLinkDao(): SjLinkDao
    abstract fun getCountDao(): SjCountDao
    abstract fun getTagDao(): SjTagDao
    abstract fun getSearchSetDao(): SjSearchSetDao
    abstract fun getDomainDao(): SjDomainDao
}




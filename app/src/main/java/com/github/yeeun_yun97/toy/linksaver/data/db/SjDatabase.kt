package com.github.yeeun_yun97.toy.linksaver.data.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.github.yeeun_yun97.toy.linksaver.data.dao.SjDao
import com.github.yeeun_yun97.toy.linksaver.data.model.*

@Database(
    entities = [
        SjTag::class,
        SjLink::class,
        SjDomain::class,
        LinkTagCrossRef::class,
        SjSearch::class,
        SearchTagCrossRef::class
    ],
    version = 1
)
abstract class SjDatabase : RoomDatabase() {
    companion object {
        private lateinit var db: SjDatabase

        fun openDatabaseForTest(applicationContext: Context) {
            this.db =
                Room.inMemoryDatabaseBuilder(applicationContext, SjDatabase::class.java).build()
        }

        fun openDatabase(applicationContext: Context) {
            if (this::db.isInitialized) {
                Log.i(javaClass.canonicalName, "there is already initialized database instance")
            } else {
                this.db =
                    Room.databaseBuilder(applicationContext, SjDatabase::class.java, "sj_database")
                        .build()
            }
        }

        fun closeDatabase() {
            if (!db.isOpen) {
                Log.i(javaClass.canonicalName, "attempt to close Database that is not open")
            } else {
                this.db.close()
            }
        }

        fun getDao(): SjDao {
            if (!this::db.isInitialized) {
                throw Exception("Database is not yet initialized")
            }
            return this.db.getDao()
        }

        fun insertFirstData() {
            /*val dao = db.getDao()

            var youtube = SjDomain(0, "유튜브", "https://m.youtube.com/watch?")
            var allchan =
                SjDomain(1, "올찬식탁", "https://m.smartstore.naver.com/allchanfood/products/")
            dao.insertEntity(youtube)
            dao.insertEntity(allchan)

            var bread = SjTag(0, "빵")
            dao.insertEntity(bread)

            var pokemonBread = SjLink(0, "돌아온 포켓몬빵", allchan.did, "6333541088")
            var pieceCake = SjLink(1, "조각 케이크", allchan.did, "718907248")
            var ytnNews = SjLink(2, "포켓몬빵 뉴스", youtube.did, "v=GJ3a33gdx7o")
            dao.insertEntity(LinkTagCrossRef(pokemonBread.lid, bread.tid))
            dao.insertEntity(LinkTagCrossRef(pieceCake.lid, bread.tid))
            dao.insertEntity(LinkTagCrossRef(ytnNews.lid, bread.tid))
            dao.insertEntity(pokemonBread)
            dao.insertEntity(pieceCake)
            dao.insertEntity(ytnNews)
             */
        }
    }

    abstract fun getDao(): SjDao
}
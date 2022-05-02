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
        // singleton object
        private lateinit var db: SjDatabase
        private const val TAG: String = "SjDatabase"

        // open database
        fun openDatabaseForTest(applicationContext: Context) {
            this.db =
                Room.inMemoryDatabaseBuilder(applicationContext, SjDatabase::class.java).build()
        }

        fun openDatabase(applicationContext: Context) {
            if (this::db.isInitialized) {
                Log.i(TAG, "there is already initialized database instance")
            } else {
                this.db =
                    Room.databaseBuilder(applicationContext, SjDatabase::class.java, "sj_database")
                        .build()
            }
        }

        // close database
        fun closeDatabase() {
            if (!db.isOpen) {
                Log.i(TAG, "attempt to close Database that is not open")
            } else {
                this.db.close()
            }
        }

        // get dao for rest of application
        fun getDao(): SjDao {
            if (!this::db.isInitialized) {
                throw Exception("Database is not yet initialized")
            }
            return this.db.getDao()
        }

    }

    protected abstract fun getDao(): SjDao

}
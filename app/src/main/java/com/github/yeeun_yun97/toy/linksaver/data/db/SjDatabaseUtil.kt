package com.github.yeeun_yun97.toy.linksaver.data.db

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.github.yeeun_yun97.toy.linksaver.data.dao.SjDao

class SjDatabaseUtil {
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
                        .addMigrations(MIGRATION_1_2)
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
}
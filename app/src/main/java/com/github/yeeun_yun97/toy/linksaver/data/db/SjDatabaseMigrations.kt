package com.github.yeeun_yun97.toy.linksaver.data.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE SjLink ADD COLUMN icon TEXT NOT NULL default ''")
        database.execSQL("ALTER TABLE SjLink ADD COLUMN preview TEXT NOT NULL default ''")
        database.execSQL("ALTER TABLE SjLink ADD COLUMN type TEXT NOT NULL default 'link'")

        database.execSQL("CREATE INDEX index_SjDomain_name ON SjDomain (name)")
        database.execSQL("CREATE UNIQUE INDEX index_SjDomain_url ON SjDomain (url)")

        database.execSQL("CREATE INDEX index_SjLink_name ON SjLink (name)")
        database.execSQL("CREATE INDEX index_SjLink_did ON SjLink (did)")
        database.execSQL("CREATE INDEX index_SjLink_type ON SjLink (type)")

        database.execSQL("CREATE UNIQUE INDEX index_SjTag_name ON SjTag (name)")

        database.execSQL("CREATE INDEX index_SjSearch_keyword ON SjSearch (keyword)")
    }
}

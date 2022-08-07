package com.github.yeeun_yun97.toy.linksaver.application

import android.content.Context
import androidx.room.Room
import com.github.yeeun_yun97.toy.linksaver.data.dao.*
import com.github.yeeun_yun97.toy.linksaver.data.db.SjDatabase
import com.github.yeeun_yun97.toy.linksaver.data.repository.SjDataStoreRepository
import com.github.yeeun_yun97.toy.linksaver.data.repository.SjNetworkRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Named("test_db")
    fun provideInMemoryDb(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(
            context,
            SjDatabase::class.java
        ).allowMainThreadQueries().build()
}

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            SjDatabase::class.java,
            "user_db"
        ).fallbackToDestructiveMigration()
            .build()

    @Provides
    fun providesCountDao(db: SjDatabase) : SjCountDao = db.getCountDao()

    @Provides
    fun providesShareDao(db: SjDatabase) : SjShareDao = db.getShareDao()

    @Provides
    fun providesLinkDao(db: SjDatabase) : SjLinkDao = db.getLinkDao()

    @Provides
    fun providesSearchSetDao(db: SjDatabase) : SjSearchSetDao = db.getSearchSetDao()

    @Provides
    fun providesDomainDao(db: SjDatabase) : SjDomainDao = db.getDomainDao()

    @Provides
    fun providesTagDao(db: SjDatabase) : SjTagDao = db.getTagDao()
}

@Module
@InstallIn(SingletonComponent::class)
class OtherRepoModule {
    @Provides
    @Singleton
    fun providesDataStoreRepo(): SjDataStoreRepository = SjDataStoreRepository.getInstance()

    @Provides
    @Singleton
    fun providesNetworkRepo(): SjNetworkRepository = SjNetworkRepository.newInstance()
}
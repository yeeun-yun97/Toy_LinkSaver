package com.github.yeeun_yun97.toy.linksaver.application

import com.github.yeeun_yun97.toy.linksaver.data.dao.SjDomainDao
import com.github.yeeun_yun97.toy.linksaver.data.dao.SjLinkDao
import com.github.yeeun_yun97.toy.linksaver.data.dao.SjSearchSetDao
import com.github.yeeun_yun97.toy.linksaver.data.dao.SjTagDao
import com.github.yeeun_yun97.toy.linksaver.data.db.SjDatabaseUtil
import com.github.yeeun_yun97.toy.linksaver.data.repository.SjDataStoreRepository
import com.github.yeeun_yun97.toy.linksaver.data.repository.SjNetworkRepository
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DaoModule{
    @Provides
    @Singleton
    fun providesLinkDao() : SjLinkDao = SjDatabaseUtil.getLinkDao()

    @Provides
    @Singleton
    fun providesSearchSetDao() : SjSearchSetDao = SjDatabaseUtil.getSearchSetDao()

    @Provides
    @Singleton
    fun providesDomainDao() : SjDomainDao = SjDatabaseUtil.getDomainDao()

    @Provides
    @Singleton
    fun providesTagDao() : SjTagDao = SjDatabaseUtil.getTagDao()
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
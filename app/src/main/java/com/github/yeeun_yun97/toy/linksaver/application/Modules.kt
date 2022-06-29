package com.github.yeeun_yun97.toy.linksaver.application

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
class RoomRepoModule {
    @Provides
    @Singleton
    fun providesLinkRepo(): SjLinkRepository = SjLinkRepository.getInstance()

    @Provides
    @Singleton
    fun providesDomainRepo(): SjDomainRepository = SjDomainRepository.getInstance()

    @Provides
    @Singleton
    fun providesSearchSetRepo(): SjSearchSetRepository = SjSearchSetRepository.getInstance()

    @Provides
    @Singleton
    fun providesTagRepo(): SjTagRepository = SjTagRepository.getInstance()

    @Provides
    @Singleton
    fun providesVideoRepo(): SjVideoRepository = SjVideoRepository.getInstance()
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
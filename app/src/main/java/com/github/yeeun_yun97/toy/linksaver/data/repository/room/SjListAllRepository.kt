package com.github.yeeun_yun97.toy.linksaver.data.repository.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.github.yeeun_yun97.toy.linksaver.data.dao.SjShareDao
import com.github.yeeun_yun97.toy.linksaver.data.model.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SjListAllRepository @Inject constructor(
    val shareDao: SjShareDao
) {
    val groupList = shareDao.getTagGroupLiveData()
    val tagList = shareDao.getTagLiveData()
    private val _domainList = shareDao.getDomainLiveData()
    private val _linkList = shareDao.getLinkLiveData()

    val domainList: LiveData<List<SjDomainForShare>> = Transformations.map(_domainList) {
        convertDomainsForShare(it)
    }
    val linkList: LiveData<List<SjLinkForShare>> = Transformations.map(_linkList) {
        convertLinkForShare(it)
    }

    private fun convertDomainsForShare(it: List<SjDomainWithTags>): List<SjDomainForShare> {
        val list = mutableListOf<SjDomainForShare>()
        for (item in it) {
            val tidList = mutableListOf<Int>()
            for (tag in item.tags) {
                tidList.add(tag.tid)
            }
            list.add(
                SjDomainForShare(
                    item.domain.did,
                    item.domain.name,
                    item.domain.url,
                    tidList
                )
            )
        }
        return list
    }

    private fun convertLinkForShare(it: List<SjLinkWithTags>): List<SjLinkForShare> {
        val list = mutableListOf<SjLinkForShare>()
        for (item in it) {
            val tidList = mutableListOf<Int>()
            for (tag in item.tags) {
                tidList.add(tag.tid)
            }
            list.add(
                SjLinkForShare(
                    item.link.lid,
                    item.link.name,
                    item.link.did,
                    item.link.url,
                    item.link.icon,
                    item.link.preview,
                    item.link.type,
                    tidList
                )
            )
        }
        return list
    }


}
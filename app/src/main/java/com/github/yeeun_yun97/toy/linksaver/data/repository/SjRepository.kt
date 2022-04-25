package com.github.yeeun_yun97.toy.linksaver.data.repository

import androidx.lifecycle.LiveData
import com.github.yeeun_yun97.toy.linksaver.data.dao.SjDao
import com.github.yeeun_yun97.toy.linksaver.data.db.SjDatabase
import com.github.yeeun_yun97.toy.linksaver.data.model.SjDomain
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLink
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SjRepository {
    val dao: SjDao = SjDatabase.getDao()
    val domains: LiveData<List<SjDomain>> = dao.getAllDomains()
    val links: LiveData<List<SjLink>> = dao.getAllLinks()
    val linksWithDomains = SjDatabase.getDao().getLinksAndDomain()
    val tags: LiveData<List<SjTag>> = dao.getAllTags()

    fun insertDomain(newDomain: SjDomain) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.insertDomain(newDomain)
        }
    }

    fun insertLink(domain: SjDomain, newLink: SjLink, tags: List<SjTag>) {
        CoroutineScope(Dispatchers.IO).launch {
            //set link domain
            newLink.did = domain.did

            //insert newLink
            dao.insertLink(newLink)

            //and insert link-tag-cross-ref
            //later.
        }
    }

    fun insertTag(newTag: SjTag) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.insertTag(newTag)
        }
    }


    fun deleteDomain(domain: SjDomain) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteDomain(domain)
            //링크에서 도메인을 참조하고 있을 수 있으니,
            //확인하고, 있으면 지우는 작업을 하지 않고,
            //사용자에게 알릴 수 있으면 좋겠다.
        }
    }

    fun deleteLink(link: SjLink) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteLink(link)
            //링크와 태그 크로스 레프 객체에서 참조하고 있을 수 있으니,
            //마찬가지로 확인하고, 있으면 지우지 말고 알리기
        }
    }

    fun deleteTag(tag: SjTag) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteTag(tag)
            //링크와 태그 크로스 레프 객체에서 참조하고 있을 수 있으니,
            //마찬가지로 확인하고, 있으면 지우지 말고 알리기
        }
    }
}
package com.github.yeeun_yun97.toy.linksaver.data

import com.github.yeeun_yun97.toy.linksaver.data.basic.RoomDBBasicTest
import com.github.yeeun_yun97.toy.linksaver.data.model.LinkTagCrossRef
import com.github.yeeun_yun97.toy.linksaver.data.model.SearchTagCrossRef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class RoomDBDeleteTest : RoomDBBasicTest() {

    @Test
    fun deleteDomain() {
        runBlocking(Dispatchers.IO) {
            val domainId = async { dao.insertDomain(domain) }
            val deleteJob = launch { dao.deleteDomain(domain.copy(did = domainId.await().toInt())) }
            deleteJob.join()
            val assertJob = launch {
                Assert.assertEquals(0, dao.getDomainCount())
            }
            assertJob.join()
        }
    }

    @Test
    fun deleteTag() {
        runBlocking(Dispatchers.IO) {
            val tagId = async {dao.insertTag(tag)}
            val deleteJob = launch {dao.deleteTag(tag.copy(tid= tagId.await().toInt()))}
            deleteJob.join()
            val assertJob = launch{
                Assert.assertEquals(0, dao.getTagCount())
            }
            assertJob.join()
        }
    }

    @Test
    fun deleteLink() {
        runBlocking(Dispatchers.IO) {
            val domainId = async { dao.insertDomain(domain) }
            val linkId = async {dao.insertLink(link.apply{did=domainId.await().toInt()})}
            val deleteJob = launch {dao.deleteLink(link.copy(lid= linkId.await().toInt()))}
            deleteJob.join()
            val assertJob = launch{
                Assert.assertEquals(0, dao.getLinkCount())
            }
            assertJob.join()
        }
    }

    @Test
    fun deleteLinkTagCrossRef() {
        var linkTagCrossRef = LinkTagCrossRef(0,0)
        runBlocking(Dispatchers.IO) {
            val domainId = async { dao.insertDomain(domain) }
            val linkId = async {dao.insertLink(link.apply{did=domainId.await().toInt()})}
            val tagId = async {dao.insertTag(tag)}
            val insertLinkTagCrossRefJob = launch{
                linkTagCrossRef=LinkTagCrossRef(lid=linkId.await().toInt(),tid=tagId.await().toInt())
                dao.insertLinkTagCrossRefs(linkTagCrossRef)
            }
            insertLinkTagCrossRefJob.join()
            val deleteJob = launch {dao.deleteLinkTagCrossRefs(linkTagCrossRef)}
            deleteJob.join()
            val assertJob = launch{
                Assert.assertEquals(0, dao.getLinkTagCrossRefCount())
            }
            assertJob.join()
        }
    }

    @Test
    fun deleteSearchTagCrossRef() {
        var searchTagCrossRef = SearchTagCrossRef(0,0)
        runBlocking(Dispatchers.IO) {
            val searchId = async {dao.insertSearch(search)}
            val tagId = async {dao.insertTag(tag)}
            val insertSearchTagCrossRefJob = launch{
                searchTagCrossRef=SearchTagCrossRef(sid=searchId.await().toInt(),tid=tagId.await().toInt())
                dao.insertSearchTagCrossRefs(searchTagCrossRef)
            }
            insertSearchTagCrossRefJob.join()
            val deleteJob = launch {dao.deleteSearchTagCrossRefs(searchTagCrossRef)}
            deleteJob.join()
            val assertJob = launch{
                Assert.assertEquals(0, dao.getLinkTagCrossRefCount())
            }
            assertJob.join()
        }
    }


}
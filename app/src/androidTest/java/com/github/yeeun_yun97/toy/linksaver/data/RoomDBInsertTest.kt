package com.github.yeeun_yun97.toy.linksaver.data

import com.github.yeeun_yun97.toy.linksaver.data.basic.RoomDBBasicTest
import com.github.yeeun_yun97.toy.linksaver.data.model.*
import kotlinx.coroutines.*
import org.junit.Assert
import org.junit.Test

class RoomDBInsertTest : RoomDBBasicTest() {

    @Test
    fun insertLinkTagCrossRef() {
        runBlocking(Dispatchers.IO) {
            val insertJob = async {
                val domainId = async { dao.insertDomain(domain) }
                val linkId = async { dao.insertLink(link.apply { did = domainId.await().toInt() }) }
                val tagId = async { dao.insertTag(tag) }
                val linkTagCrossRef = LinkTagCrossRef(
                    tid = tagId.await().toInt(),
                    lid = linkId.await().toInt()
                )
                dao.insertLinkTagCrossRefs(linkTagCrossRef)
            }

            val assertJob = launch {
                Assert.assertEquals(1, insertJob.await()[0].toInt())
            }
            assertJob.join()
        }
    }

    @Test
    fun insertSearchTagCrossRef() {
        runBlocking(Dispatchers.IO) {
            val insertJob = async {
                val tagId = async { dao.insertTag(tag) }
                val searchId = async { dao.insertSearch(search) }
                val searchTagCrossRef = SearchTagCrossRef(
                    sid = searchId.await().toInt(),
                    tid = tagId.await().toInt()
                )
                dao.insertSearchTagCrossRefs(searchTagCrossRef)
            }

            val assertJob = launch {
                Assert.assertEquals(1, insertJob.await()[0].toInt())
            }
            assertJob.join()
        }
    }

    @Test
    fun insertDomainTest() {
        runBlocking(Dispatchers.IO){
            val domainId = async { dao.insertDomain(domain) }
            Assert.assertEquals(1,domainId.await().toInt())
        }
    }

    @Test
    fun insertTagTest() {
        runBlocking(Dispatchers.IO){
            val tagId = async { dao.insertTag(tag) }
            Assert.assertEquals(1,tagId.await().toInt())
        }
    }

    @Test
    fun insertLinkTest() {
        runBlocking(Dispatchers.IO){
            val domainId = async { dao.insertDomain(domain) }
            val linkId = async { dao.insertLink(link.apply { did = domainId.await().toInt() }) }
            Assert.assertEquals(1, linkId.await().toInt())
        }
    }

    //live data observing not working
    //don't know why..
    /*
    @Test
    fun insertSearchTagCrossRef() {
        runBlocking {
            var searchTagCrossRef = SearchTagCrossRef(-1, -1)
            launch(Dispatchers.IO) {
                val insertTagJob = async { dao.insertTag(tag) }
                val insertSearchJob = async { dao.insertSearch(search) }
                val insertSearchTagCrossRefJob = async {
                    val sid = insertSearchJob.await().toInt()
                    Log.d("Test insert Search", sid.toString())
                    searchTagCrossRef = SearchTagCrossRef(
                        sid = sid,
                        tid = insertTagJob.await().toInt()
                    )
                    dao.insertSearchTagCrossRefs(searchTagCrossRef)
                }
                Log.d("Test insert Id", insertSearchTagCrossRefJob.await().toString())
            }
            val queryJob = async(Dispatchers.IO){
                dao.getAllSearch()
            }
            val assertJob = launch(Dispatchers.Main) {
                val result = queryJob.await().blockingObserve(time=10)?.await()
                Assert.assertEquals(search, result?.get(0)?.search)
            }
            assertJob.join()
        }
    }
    */

}
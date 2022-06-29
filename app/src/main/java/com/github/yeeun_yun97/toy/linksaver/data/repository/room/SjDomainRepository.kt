package com.github.yeeun_yun97.toy.linksaver.data.repository.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.yeeun_yun97.toy.linksaver.data.db.SjDatabaseUtil
import com.github.yeeun_yun97.toy.linksaver.data.model.SjDomain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SjDomainRepository private constructor() {

    private val dao = SjDatabaseUtil.getDomainDao()

    companion object {
        // singleton object
        private lateinit var repo: SjDomainRepository

        fun getInstance(): SjDomainRepository {
            if (!this::repo.isInitialized) {
                repo = SjDomainRepository()
            }
            return repo
        }
    }


    val _domains = MutableLiveData<List<SjDomain>>()
    val domains: LiveData<List<SjDomain>> = _domains

    suspend fun selectDomainByDid(did: Int): SjDomain = dao.selectDomainByDid(did)


    // insert methods
    suspend fun insertDomain(newDomain: SjDomain) =
        dao.insertDomain(newDomain)

    suspend fun updateDomain(domain: SjDomain) =
        dao.updateDomain(domain)


    // delete methods
    suspend fun deleteDomain(domain: SjDomain) =
        CoroutineScope(Dispatchers.IO).launch {
            // move related links to default domain
            val job = launch {
                dao.updateLinksToDefaultDomainByDid(domain.did)
            }
            job.join()
            //wait and delete
            dao.deleteDomain(domain)
        }

}
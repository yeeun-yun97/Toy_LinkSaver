package com.github.yeeun_yun97.toy.linksaver.data.repository.room.link

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.yeeun_yun97.toy.linksaver.data.dao.SjLinkDao
import com.github.yeeun_yun97.toy.linksaver.data.model.*
import com.github.yeeun_yun97.toy.linksaver.ui.component.SjUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SjViewLinkRepository @Inject constructor(
    private val dao: SjLinkDao
) {
    private val _link = MutableLiveData<LinkDetailValue>()
    val link: LiveData<LinkDetailValue> get() = _link

    // manage liveData
    fun postLink(lid: Int) =
        CoroutineScope(Dispatchers.IO).launch {
            val entity = dao.selectLinkByLid(lid)
            val vo = LinkDetailValue(
                lid = entity.link.lid,
                name = entity.link.name,
                url = entity.link.url,
                preview = entity.link.preview,
                isVideo = when (entity.link.type) {
                    ELinkType.video -> true
                    ELinkType.link -> false
                },
                isYoutubeVideo =
                entity.link.type == ELinkType.video &&
                        SjUtil.checkYoutubePrefix(
                            entity.link.url
                        ),
                tags = entity.tags
            )
            _link.postValue(vo)
        }


    // delete
    fun deleteLinkByLid(lid: Int) =
        CoroutineScope(Dispatchers.IO).launch {
            //delete all related tag refs
            val deleteRefs = launch {
                dao.deleteLinkTagCrossRefsByLid(lid)
            }
            deleteRefs.join()
            //wait and delete
            dao.deleteLinkByLid(lid)
        }


}
package com.github.yeeun_yun97.toy.linksaver.viewmodel.link

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.github.yeeun_yun97.toy.linksaver.data.model.LinkDetailValue
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.link.SjViewLinkRepository
import com.github.yeeun_yun97.toy.linksaver.viewmodel.base.SjBaseViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewLinkViewModel @Inject constructor(
    private val linkRepo : SjViewLinkRepository
) : SjBaseViewModelImpl() {

    var lid: Int? = null
        set(data) {
            field = data
            refreshData()
        }

   val link: LiveData<LinkDetailValue> = linkRepo.link

    // binding variables
    val bindingLinkName: LiveData<String> = Transformations.map(link) { it.name }
    val bindingTags: LiveData<List<SjTag>> = Transformations.map(link) { it.tags }
    val bindingFullUrl: LiveData<String> = Transformations.map(link) { it.url }

    override fun refreshData() {
        linkRepo.postLink(lid!!)
    }

    fun deleteLink() {
        linkRepo.deleteLinkByLid(lid!!)
    }

}
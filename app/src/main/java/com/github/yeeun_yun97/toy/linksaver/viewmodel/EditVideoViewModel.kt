package com.github.yeeun_yun97.toy.linksaver.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.data.repository.SjNetworkRepository
import com.github.yeeun_yun97.toy.linksaver.viewmodel.basic.BasicViewModelWithRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditVideoViewModel : BasicViewModelWithRepository() {
    private val networkRepository = SjNetworkRepository.newInstance()

    // two way binding liveDatas
    val name = MutableLiveData<String>()
    val url = MutableLiveData<String>()

    val selectedTags = mutableListOf<SjTag>()
    val tagList = repository.tags

    fun loadUrl(url: String) {
        viewModelScope.launch(Dispatchers.IO){
                this@EditVideoViewModel.url.postValue(url)

            // 이름 불러오기
            launch{
                val title = networkRepository.getTitleOf(url)
                name.postValue(title)
            }

            // 미리보기 불러오기
            launch{
                networkRepository.getPreviewOf(url)

            }

            // 아이콘 불러오기


            // 동영상 불러오


        }
    }


}
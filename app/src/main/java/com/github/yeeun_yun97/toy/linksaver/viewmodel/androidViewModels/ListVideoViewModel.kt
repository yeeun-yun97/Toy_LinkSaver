package com.github.yeeun_yun97.toy.linksaver.viewmodel.androidViewModels

import android.app.Application
import android.util.SparseArray
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.yeeun_yun97.toy.linksaver.data.model.VideoData
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.link.SjListVideoRepository
import com.github.yeeun_yun97.toy.linksaver.ui.component.SjYoutubeExtractListener
import com.github.yeeun_yun97.toy.linksaver.ui.component.SjYoutubeExtractor
import com.github.yeeun_yun97.toy.linksaver.viewmodel.base.SjUsePrivateModeViewModelAndroidImpl
import com.google.android.exoplayer2.MediaItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListVideoViewModel @Inject constructor(
    application: Application,
    private val videoRepo : SjListVideoRepository
) : SjUsePrivateModeViewModelAndroidImpl(application) {
    // setting preview start and end
    private val START_MS: Long = 1000
    private val END_MS: Long = 16000

    // liveDataList
    private val _playList = MutableLiveData(mutableListOf<MediaItem>())
    val playList: LiveData<MutableList<MediaItem>> get() = _playList
    val videoDatas : LiveData<List<VideoData>> = videoRepo.linksVideo

    init {
        videoDatas.observeForever{
            loadIntoPlayList(it)
        }
    }

    override fun refreshData() {
        if (isPrivateMode) {
            videoRepo.postVideosPublic()
        } else {
            videoRepo.postAllVideos()
        }
    }

    private fun loadIntoPlayList(dataList: List<VideoData>) {
        val mediaItems: SparseArray<MediaItem> = SparseArray()
        for (i in dataList.indices) {
            val videoData = dataList[i]
            if (videoData.isYoutubeVideo) {
                val listener = object : SjYoutubeExtractListener {
                    override fun onExtractionComplete(extractedUrl: String) {
                        saveMediaItem(i, extractedUrl, mediaItems, dataList.size)
                    }
                }
                SjYoutubeExtractor(getApplication(), listener).extract(videoData.url)
            } else {
                saveMediaItem(i, videoData.url, mediaItems, dataList.size)
            }
        }
    }

    private fun saveMediaItem(
        position: Int,
        url: String,
        sparseArray: SparseArray<MediaItem>,
        length: Int
    ) {
        sparseArray.append(position, getMediaItemFromUrl(url))
        if (sparseArray.size() == length) {
            val mediaItemList = mutableListOf<MediaItem>()
            for (i in 0 until length) {
                mediaItemList.add(i, sparseArray[i])
            }
            _playList.postValue(mediaItemList)
        }
    }

    private fun getMediaItemFromUrl(url: String): MediaItem {
        return MediaItem.Builder()
            .setUri(url)
            .setClippingConfiguration(
                MediaItem.ClippingConfiguration.Builder()
                    .setStartPositionMs(START_MS)
                    .setEndPositionMs(END_MS)
                    .build()
            ).build()
    }


}
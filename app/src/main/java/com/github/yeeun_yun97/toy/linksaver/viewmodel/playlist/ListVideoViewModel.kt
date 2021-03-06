package com.github.yeeun_yun97.toy.linksaver.viewmodel.playlist

import android.app.Application
import android.util.SparseArray
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.yeeun_yun97.toy.linksaver.data.repository.SjVideoRepository
import com.github.yeeun_yun97.toy.linksaver.ui.component.SjYoutubeExtractListener
import com.github.yeeun_yun97.toy.linksaver.ui.component.SjYoutubeExtractor
import com.google.android.exoplayer2.MediaItem


class ListVideoViewModel(application: Application) : AndroidViewModel(application) {
    // setting preview start and end
    private val START_MS: Long = 1000
    private val END_MS: Long = 16000

    // repository
    val repository = SjVideoRepository.getInstance()

    // liveDatats
    private val _playList = MutableLiveData(mutableListOf<MediaItem>())
    val allVideoData = repository.allVideoData
    val playList: LiveData<MutableList<MediaItem>> get() = _playList

    fun loadPlayList() {
        val videoDatas = allVideoData.value!!
        val mediaItems: SparseArray<MediaItem> = SparseArray()
        for (i in videoDatas.indices) {
            val videoData = videoDatas[i]
            if (videoData.isYoutubeVideo) {
                val listener = object : SjYoutubeExtractListener {
                    override fun onExtractionComplete(extractedUrl: String) {
                        saveMediaItem(i, extractedUrl, mediaItems, videoDatas.size)
                    }
                }
                SjYoutubeExtractor(getApplication(), listener).extract(videoData.url)
            } else {
                saveMediaItem(i, videoData.url, mediaItems, videoDatas.size)
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
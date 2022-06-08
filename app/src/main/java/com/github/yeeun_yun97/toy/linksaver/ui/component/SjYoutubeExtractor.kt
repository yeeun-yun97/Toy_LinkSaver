package com.github.yeeun_yun97.toy.linksaver.ui.component

import android.content.Context
import android.util.Log
import android.util.SparseArray
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile

interface SjYoutubeExtractListener {
    fun onExtractionComplete(extractedUrl: String)
}
class SjYoutubeExtractor(context: Context, private val listener: SjYoutubeExtractListener) :
    YouTubeExtractor(context) {
    override fun onExtractionComplete(
        sparseArray: SparseArray<YtFile>?,
        vMeta: VideoMeta?
    ) {
        Log.d("SjYoutubeExtractor", "onExtractionComplete")
        if (sparseArray != null) {
            //136 no audio 247,
            val iTags = listOf(22, 18);
            for (itag in iTags) {
                val downloadUrl = sparseArray[itag]?.url
                if (downloadUrl != null) {
                    Log.d("SjYoutubeExtractor", "loaded $itag")
                    listener.onExtractionComplete(downloadUrl)
                    break
                } else {
                    Log.d("SjYoutubeExtractor", "canotload")
                }
            }
        }
    }
}
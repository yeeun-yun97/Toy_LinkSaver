package com.github.yeeun_yun97.toy.linksaver.ui.fragment

import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentPlayVideoBinding
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import android.util.SparseArray
import android.annotation.SuppressLint
import android.net.Uri
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource

class PlayVideoFragment : SjBasicFragment<FragmentPlayVideoBinding>() {

    private var _player: ExoPlayer? = null
    private val player: ExoPlayer get() = _player!!

    override fun layoutId(): Int = R.layout.fragment_play_video

    override fun onCreateView() {
        _player = ExoPlayer.Builder(requireContext()).build()
        binding.playerView.player = player
    }

    override fun onStop() {
        super.onStop()
        binding.playerView.player = null
        player.release()
        _player = null
    }

    override fun onResume() {
        super.onResume()
        val sampleLink = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
        val youtubeLink = "https://www.youtube.com/watch?v=H0M1yU6uO30"

        playExample(sampleLink)
    }

    private fun playExample(url:String) {
        val dataSourceFactory = DefaultDataSource.Factory(requireContext())
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(
            MediaItem.fromUri(Uri.parse(url))
        )

        player.setMediaSource(mediaSource)
        player.prepare()
        player.playWhenReady = true
    }

    fun playVideo(url: String) {
        val mediaItem = MediaItem.fromUri(url)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    @SuppressLint("StaticFieldLeak")
    private fun extractYoutubeUrl(url: String) {
        object : YouTubeExtractor(requireContext()) {
            override fun onExtractionComplete(ytFiles: SparseArray<YtFile>?, vMeta: VideoMeta?) {
                if (ytFiles != null) {
                    val itag = 22
                    val downloadUrl = ytFiles[itag]?.url
                    if (downloadUrl != null) {
                        playVideo(downloadUrl)
                    }
                }
            }
        }.extract(url)
    }
}
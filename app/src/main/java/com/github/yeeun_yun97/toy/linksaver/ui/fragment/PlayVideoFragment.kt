package com.github.yeeun_yun97.toy.linksaver.ui.fragment

import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentPlayVideoBinding
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import android.util.SparseArray
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.*

class PlayVideoFragment : SjBasicFragment<FragmentPlayVideoBinding>() {

    private var _player: ExoPlayer? = null
    private val player: ExoPlayer get() = _player!!

    private lateinit var exoPlayer: SimpleExoPlayer
    private lateinit var mediaSource: ProgressiveMediaSource

    //    private val cache: SimpleCache = LinkSaverApplication.cache
//    private lateinit var mHttpDataSourceFactory: HttpDataSource.Factory
//    private lateinit var mDefaultDataSourceFactory: DefaultDataSourceFactory
//    private lateinit var mCacheDataSourceFactory: DataSource.Factory


    companion object {
        fun newInstance(url: String): PlayVideoFragment {
            val fragment = PlayVideoFragment()
            fragment.arguments = Bundle().apply { putString("VIDEO_URL", url) }
            return fragment
        }
    }


    override fun layoutId(): Int = R.layout.fragment_play_video

    override fun onCreateView() {
//        _player = ExoPlayer.Builder(requireContext()).build()
//        binding.playerView.player = player

//        val applicationContext = requireActivity().applicationContext
//        val videoUrl = requireArguments().getString("VIDEO_URL")
//
//        mHttpDataSourceFactory = DefaultHttpDataSource.Factory()
//            .setAllowCrossProtocolRedirects(true)
//
//        this.mDefaultDataSourceFactory = DefaultDataSourceFactory(
//            applicationContext, mHttpDataSourceFactory
//        )
//
//        mCacheDataSourceFactory = CacheDataSource.Factory()
//            .setCache(cache)
//            .setUpstreamDataSourceFactory(mHttpDataSourceFactory)
//            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
//
//
//        exoPlayer = SimpleExoPlayer.Builder(applicationContext)
//            .setMediaSourceFactory(DefaultMediaSourceFactory(mCacheDataSourceFactory)).build()
//
//        val videoUri = Uri.parse(videoUrl)
//        val mediaItem = MediaItem.fromUri(videoUri)
//        mediaSource =
//            ProgressiveMediaSource.Factory(mCacheDataSourceFactory).createMediaSource(mediaItem)


    }

    override fun onStop() {
        super.onStop()
//        binding.playerView.player = null
//        player.release()
//        _player = null
    }

    override fun onResume() {
        super.onResume()
        binding.playerView.player = exoPlayer
        exoPlayer.playWhenReady = true
        exoPlayer.seekTo(0, 0)
        exoPlayer.setMediaSource(mediaSource, true)
        exoPlayer.prepare()
//        val sampleLink =
//            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
//        val youtubeLink = "https://www.youtube.com/watch?v=H0M1yU6uO30"
//        extractYoutubeUrl(youtubeLink)
//        playExample(sampleLink)
    }


    private fun playExample(url: String) {
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
            override fun onExtractionComplete(
                sparseArray: SparseArray<YtFile>?,
                vMeta: VideoMeta?
            ) {
                if (sparseArray != null) {
                    //136 no audio 247,
                    val iTags = listOf(22, 18);
                    for (itag in iTags) {
                        val downloadUrl = sparseArray[itag]?.url
                        if (downloadUrl != null) {
                            Log.d("tl", "loaded $itag")
                            playExample(downloadUrl)
                            break
                        } else {
                            Log.d("tl", "canotload")
                        }
                    }
                }
            }
        }.extract(url)
    }
}
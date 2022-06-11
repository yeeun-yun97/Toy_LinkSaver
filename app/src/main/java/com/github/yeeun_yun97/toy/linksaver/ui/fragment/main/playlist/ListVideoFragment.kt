package com.github.yeeun_yun97.toy.linksaver.ui.fragment.main.playlist

import android.util.Log
import android.util.SparseArray
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.yeeun_yun97.clone.ynmodule.ui.component.DataState
import com.github.yeeun_yun97.clone.ynmodule.ui.component.ViewVisibilityUtil
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentListVideoBinding
import com.github.yeeun_yun97.toy.linksaver.ui.adapter.RecyclerVideoAdapter
import com.github.yeeun_yun97.toy.linksaver.ui.adapter.VideoRecyclerViewHolder
import com.github.yeeun_yun97.toy.linksaver.ui.component.SjYoutubeExtractListener
import com.github.yeeun_yun97.toy.linksaver.ui.component.SjYoutubeExtractor
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.main.search.detail_link.DetailLinkFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.ListVideoViewModel
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.common.collect.ImmutableSet

class ListVideoFragment : SjBasicFragment<FragmentListVideoBinding>() {
    private val viewModel: ListVideoViewModel by activityViewModels()

    // control view visibility
    private lateinit var viewUtil: ViewVisibilityUtil

    private val START_MS: Long = 1000
    private val END_MS: Long = 16000

    private lateinit var manager: LinearLayoutManager
    private lateinit var adapter: RecyclerVideoAdapter

    private var _player: ExoPlayer? = null
    private val player: ExoPlayer get() = _player!!

    override fun layoutId(): Int = R.layout.fragment_list_video

    override fun onCreateView() {

        // set toolbar
        val handlerMap = hashMapOf<Int, () -> Unit>(R.id.menu_playlist to ::moveToPlaylistFragment)
        binding.toolbar.setMenu(R.menu.toolbar_menu_video_list, handlerMap = handlerMap)

        // player
        _player = ExoPlayer.Builder(requireContext()).build()
        player.repeatMode = Player.REPEAT_MODE_ONE

        // set view Util
        viewUtil = ViewVisibilityUtil(
            loadingView = binding.shimmer,
            loadedView = binding.videoRecyclerView,
            emptyView = binding.emptyGroup
        )

        // disable track types or groups
        player.trackSelectionParameters =
            player.trackSelectionParameters.buildUpon()
                .setDisabledTrackTypes(
                    ImmutableSet.of(C.TRACK_TYPE_AUDIO)
                ) // disable track type audio
                .build()

        // set adapter
        manager = LinearLayoutManager(context)
        binding.videoRecyclerView.layoutManager = manager
        adapter = RecyclerVideoAdapter(player, ::moveToDetailFragment)
        binding.videoRecyclerView.adapter = adapter

        // set view by liveData
        viewModel.playList.observe(viewLifecycleOwner, {
            if (it.isNullOrEmpty()) {
                viewUtil.state = DataState.EMPTY
            } else if(adapter.itemCount!=0){
                Log.d("playlist loaded", it.toString())
                 viewUtil.state = DataState.LOADED
                player.setMediaItems(it)
                player.prepare()
            }
        })
        viewModel.allVideoData.observe(viewLifecycleOwner, {
            if (!it.isNullOrEmpty()) {
                viewUtil.state = DataState.LOADING
            } else {
                viewUtil.state = DataState.EMPTY
            }
            val mediaItems: SparseArray<MediaItem> = SparseArray()
            for (i in it.indices) {
                val videoData = it[i]
                if (videoData.isYoutubeVideo) {
                    val listener = object : SjYoutubeExtractListener {
                        override fun onExtractionComplete(extractedUrl: String) {
                            saveMediaItem(i, extractedUrl, mediaItems, it.size)
                        }
                    }
                    SjYoutubeExtractor(requireContext(), listener).extract(videoData.url)
                } else {
                    saveMediaItem(i, videoData.url, mediaItems, it.size)
                }
            }
            adapter.setList(it)
        })

        binding.videoRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var prevPosition: Int = -1

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val position = manager.findFirstCompletelyVisibleItemPosition()
                val currentViewHolder =
                    binding.videoRecyclerView.findViewHolderForLayoutPosition(position)
                Log.d("onScroll", "prev: $prevPosition, current: $position")
                if (position != prevPosition && currentViewHolder is VideoRecyclerViewHolder) {
                    if (prevPosition != -1) {
                        val prevViewHolder =
                            binding.videoRecyclerView.findViewHolderForAdapterPosition(prevPosition)
                        if (prevViewHolder is VideoRecyclerViewHolder) {
                            prevViewHolder.playStop()
                        }
                    }
                    currentViewHolder.playStart()
                }
                prevPosition = position
            }
        })

    }


    override fun onPause() {
        super.onPause()
        // pause player
        player.pause()
    }

    override fun onStop() {
        super.onStop()
        // release player
        player.release()
        _player = null
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
            viewModel.playList.postValue(mediaItemList)
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

    private fun moveToDetailFragment(lid: Int) {
        moveToOtherFragment(DetailLinkFragment.newInstance(lid))
    }

    private fun moveToPlaylistFragment() {
        Toast.makeText(requireContext(), "구현예정", Toast.LENGTH_LONG).show()
    }


}
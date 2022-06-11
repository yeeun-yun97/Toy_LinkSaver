package com.github.yeeun_yun97.toy.linksaver.ui.fragment.main.playlist

import android.util.Log
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
        initPlayer(player)

        // set view Util
        viewUtil = ViewVisibilityUtil(
            loadingView = binding.shimmer,
            loadedView = binding.videoRecyclerView,
            emptyView = binding.emptyGroup
        )

        // set recycler adapter
        manager = LinearLayoutManager(context)
        adapter = RecyclerVideoAdapter(player, ::moveToDetailFragment)
        binding.videoRecyclerView.layoutManager = manager
        binding.videoRecyclerView.adapter = adapter

        // set recycler scroll listener
        binding.videoRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var prevPosition: Int = -1
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                prevPosition = scrollAndPlay(prevPosition)
            }
        })


        // set view by liveData
        viewModel.playList.observe(viewLifecycleOwner, {
            if (adapter.itemCount != 0) {
                viewUtil.state = DataState.LOADED
                setMediaItemsAndPrepare(it)
            }
        })
        viewModel.allVideoData.observe(viewLifecycleOwner, {
            if (!it.isNullOrEmpty()) {
                viewUtil.state = DataState.LOADING
                viewModel.loadPlayList()
            } else {
                viewUtil.state = DataState.EMPTY
            }
            adapter.setList(it)
        })
    }

    override fun onStart() {
        super.onStart()
        binding.videoRecyclerView.scrollToPosition(0)
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

    private fun scrollAndPlay(prevPosition: Int): Int {
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
        return position
    }

    private fun setMediaItemsAndPrepare(mediaItems: List<MediaItem>) {
        player.setMediaItems(mediaItems)
        player.prepare()
    }

    private fun initPlayer(player: ExoPlayer) {
        // player replays one video
        player.repeatMode = Player.REPEAT_MODE_ONE

        // disable track types or groups
        player.trackSelectionParameters =
            player.trackSelectionParameters.buildUpon()
                .setDisabledTrackTypes(
                    ImmutableSet.of(C.TRACK_TYPE_AUDIO)
                ) // disable track type audio
                .build()
    }

    private fun moveToDetailFragment(lid: Int) {
        moveToOtherFragment(DetailLinkFragment.newInstance(lid))
    }

    private fun moveToPlaylistFragment() {
        Toast.makeText(requireContext(), "구현예정", Toast.LENGTH_LONG).show()
    }


}
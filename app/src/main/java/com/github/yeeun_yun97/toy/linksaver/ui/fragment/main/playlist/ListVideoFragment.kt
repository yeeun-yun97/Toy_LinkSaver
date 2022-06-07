package com.github.yeeun_yun97.toy.linksaver.ui.fragment.main.playlist

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.VideoData
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentListVideoBinding
import com.github.yeeun_yun97.toy.linksaver.ui.adapter.RecyclerVideoAdapter
import com.github.yeeun_yun97.toy.linksaver.ui.adapter.VideoRecyclerViewHolder
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.main.search.detail_link.DetailLinkFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.ListVideoViewModel
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.common.collect.ImmutableSet

class ListVideoFragment : SjBasicFragment<FragmentListVideoBinding>() {

    private val viewModel: ListVideoViewModel by activityViewModels()

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

        viewModel.allVideoList.observe(viewLifecycleOwner, {
            val videos = mutableListOf<VideoData>()
            for (vid in it) {
                Log.d("비디오 불러옴", vid.toString())
                videos.add(
                    VideoData(
                        vid.link.lid,
                        vid.domain.url + vid.link.url,
                        vid.link.name,
                        vid.link.preview,
                        vid.tags
                    )
                )
            }
            adapter.setList(videos)
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


    private fun moveToDetailFragment(lid: Int) {
//        moveToOtherFragment(DetailVideoFragment.newInstance(lid))
        moveToOtherFragment(DetailLinkFragment.newInstance(lid))
    }

    private fun moveToPlaylistFragment() {
        Toast.makeText(requireContext(), "구현예정", Toast.LENGTH_LONG).show()
    }
}
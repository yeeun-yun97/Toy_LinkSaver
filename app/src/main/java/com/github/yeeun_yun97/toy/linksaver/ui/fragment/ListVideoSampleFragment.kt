package com.github.yeeun_yun97.toy.linksaver.ui.fragment

import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentListVideoBinding
import com.github.yeeun_yun97.toy.linksaver.ui.adapter.RecyclerVideoAdapter
import com.github.yeeun_yun97.toy.linksaver.ui.adapter.VideoRecyclerViewHolder
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.ListVideoViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player

class ListVideoSampleFragment : SjBasicFragment<FragmentListVideoBinding>() {

    private val viewModel: ListVideoViewModel by activityViewModels()

    private var _player: ExoPlayer? = null
    private val player: ExoPlayer get() = _player!!

    override fun layoutId(): Int = R.layout.fragment_list_video

    override fun onCreateView() {
        val handlerMap = hashMapOf<Int, () -> Unit>(R.id.menu_playlist to ::moveToPlaylistFragment)
        binding.toolbar.setMenu(R.menu.toolbar_menu_video_list, handlerMap = handlerMap)

        _player = ExoPlayer.Builder(requireContext()).build()
        player.repeatMode = Player.REPEAT_MODE_ONE

        val manager = LinearLayoutManager(context)
        binding.videoRecyclerView.layoutManager = manager
        val adapter = RecyclerVideoAdapter(player, ::moveToDetailFragment)
        binding.videoRecyclerView.adapter = adapter

        adapter.setList(viewModel.getDataList())

        binding.videoRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var prevPosition: Int = -1

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val position = manager.findFirstCompletelyVisibleItemPosition()
                val currentViewHolder = binding.videoRecyclerView.findViewHolderForLayoutPosition(position)
                if (currentViewHolder is VideoRecyclerViewHolder) {
                    if (prevPosition != -1) {
                        val prevViewHolder = binding.videoRecyclerView.findViewHolderForAdapterPosition(prevPosition)
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

    override fun onStop() {
        super.onStop()

        // release player
        player.release()
        _player = null
    }


    private fun moveToDetailFragment(lid:Int) {
        moveToOtherFragment(DetailVideoFragment.newInstance(0))
    }

    private fun moveToPlaylistFragment() {
        Toast.makeText(requireContext(), "구현예정", Toast.LENGTH_LONG).show()
    }
}
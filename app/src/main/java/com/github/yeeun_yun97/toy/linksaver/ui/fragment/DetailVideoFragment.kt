package com.github.yeeun_yun97.toy.linksaver.ui.fragment

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentDetailVideoBinding
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.detail_link.DetailLinkViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

class DetailVideoFragment : SjBasicFragment<FragmentDetailVideoBinding>() {
    private val viewModel: DetailLinkViewModel by activityViewModels()
    private var lid: Int = -1

    private var _player: ExoPlayer? = null
    private val player: ExoPlayer get() = _player!!

    companion object {
        fun newInstance(lid: Int): DetailVideoFragment {
            val fragment = DetailVideoFragment()
            fragment.arguments = Bundle().apply {
                putInt("lid", lid)
            }
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_detail_video


    override fun onCreateView() {
        // get lid from argument
        lid = requireArguments().getInt("lid", -1)
        if (lid > 0) {
            viewModel.loadLinkData(lid)
        }

        viewModel.bindingFullUrl.observe(viewLifecycleOwner,{ fullUrl->
            val mediaItem = MediaItem.fromUri(fullUrl)
            player.setMediaItem(mediaItem)
            player.prepare()
        })


        //        schedulePreloadWork("https://www.youtube.com/watch?v=H0M1yU6uO30")

    }

    //    private fun schedulePreloadWork(videoUrl: String) {
//        val workManager = WorkManager.getInstance(requireActivity().applicationContext)
//        val videoPreloadWorker = VideoPreloadWorker.buildWorkRequest(videoUrl)
//        workManager.enqueueUniqueWork(
//            "VideoPreloadWorker",
//            ExistingWorkPolicy.KEEP,
//            videoPreloadWorker
//        )
//    }

    override fun onStart() {
        super.onStart()

        /* Exoplayer docs clone */
        // create player
        _player = ExoPlayer.Builder(requireContext()).build()

        // connect player to view
        binding.playerView.player = player

    }

    override fun onResume() {
        super.onResume()
        player.play()
    }

    override fun onStop() {
        super.onStop()

        // release player
        player.release()
        _player = null
    }

    private fun moveToPlayFragment() {
        val url = "https://www.youtube.com/watch?v=H0M1yU6uO30"
        moveToOtherFragment(PlayVideoFragment.newInstance(url))
        Toast.makeText(context, "pressed", Toast.LENGTH_LONG).show()
    }


}
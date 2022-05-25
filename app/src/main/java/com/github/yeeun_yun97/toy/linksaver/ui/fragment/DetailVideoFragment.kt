package com.github.yeeun_yun97.toy.linksaver.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLink
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentDetailVideoBinding
import com.github.yeeun_yun97.toy.linksaver.ui.activity.EditLinkActivity
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.DetailLinkViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

class DetailVideoFragment : SjBasicFragment<FragmentDetailVideoBinding>() {
    private val viewModel: DetailLinkViewModel by activityViewModels()
    private var lid: Int = -1

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
        lid = requireArguments().getInt("lid")

        // set variable of binding
        binding.viewModel = viewModel

        // set linkdata user to click event
        viewModel.link.observe(viewLifecycleOwner, { data ->
            // edit
            binding.editImageView.setOnClickListener { startEditActivityToUpdate(data.link.lid) }

            // delete
            binding.deleteImageView.setOnClickListener { deleteLink(data.link, data.tags) }
        })

    }

    override fun onResume() {
        super.onResume()
        viewModel.loadLinkData(lid)

        /* Exoplayer docs clone */
        // create player
        val player = ExoPlayer.Builder(requireContext()).build()

        //connect player to view
        val playerView = binding.playerView
        playerView.player=player

        val exampleUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4"
        val exampleUrl2 = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
        val mediaItem = MediaItem.fromUri(exampleUrl)
        val mediaItem2 = MediaItem.fromUri(exampleUrl2)
        player.addMediaItem(mediaItem)
        player.addMediaItem(mediaItem2)
        player.prepare()
        player.play()
    }

    override fun onStop() {
        super.onStop()
        // clear viewModel data when screen not visible
        viewModel.clearData()
    }

    private fun deleteLink(link: SjLink, tags: List<SjTag>) {
        viewModel.deleteLink(link, tags)
        popBack()
    }

    private fun moveToPlayFragment() {
        val url = "https://www.youtube.com/watch?v=H0M1yU6uO30"
        moveToOtherFragment(PlayVideoFragment.newInstance(url))
        Toast.makeText(context, "pressed", Toast.LENGTH_LONG).show()
    }

    private fun startEditActivityToUpdate(lid: Int) {
        val intent = Intent(requireContext(), EditLinkActivity::class.java)
        intent.putExtra("lid", lid)
        startActivity(intent)
    }

    private fun isStringTypeUrl(url: String): Boolean {
        return url.startsWith("http://") || url.startsWith("https://")
    }

    private fun startWebBrowser(url: String) {
        if (isStringTypeUrl(url)) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }
}
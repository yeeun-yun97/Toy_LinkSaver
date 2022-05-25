package com.github.yeeun_yun97.toy.linksaver.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.github.yeeun_yun97.clone.ynmodule.ui.component.SjImageViewUtil
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLink
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentDetailLinkBinding
import com.github.yeeun_yun97.toy.linksaver.ui.activity.EditLinkActivity
import com.github.yeeun_yun97.toy.linksaver.ui.component.SjTagChip
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.DetailLinkViewModel

class DetailLinkFragment : SjBasicFragment<FragmentDetailLinkBinding>() {
    private val viewModel: DetailLinkViewModel by activityViewModels()
    private var lid: Int = -1

    companion object {
        fun newInstance(lid: Int): DetailLinkFragment {
            val fragment = DetailLinkFragment()
            fragment.arguments = Bundle().apply {
                putInt("lid", lid)
            }
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_detail_link

    override fun onStart() {
        super.onStart()
        SjImageViewUtil.setDefaultImage(
            fragment = this,
            imageView = binding.previewImageView,
            defaultImage = R.drawable.ic_baseline_web_24
        )
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadLinkData(lid)
    }

    override fun onStop() {
        super.onStop()
        // clear viewModel data when screen not visible
        viewModel.clearData()
    }

    override fun onCreateView() {
        // get lid from argument
        lid = requireArguments().getInt("lid")

        // set variable of binding
        binding.viewModel = viewModel

        // set toolbar Menu
//        binding.toolbar.setMenu(
//            R.menu.menu_link_datail,
//            hashMapOf(R.id.playItem to ::moveToPlayFragment)
//        )

        // show tags or show empty view
        viewModel.tags.observe(viewLifecycleOwner, { tagList ->
            if (tagList.isEmpty()) {
                binding.tagEmptyGroup.visibility = View.VISIBLE
            } else {
                binding.tagEmptyGroup.visibility = View.GONE
                binding.tagChipGroup.removeAllViews()
                for (tag in tagList) {
                    binding.tagChipGroup.addView(
                        SjTagChip(
                            requireContext(),
                            tag
                        ).apply { setViewMode() })
                }
            }
        })

        // set linkdata user to click event
        viewModel.link.observe(viewLifecycleOwner, { data ->
            // edit
            binding.editImageView.setOnClickListener { startEditActivityToUpdate(data.link.lid) }

            // delete
            binding.deleteImageView.setOnClickListener { deleteLink(data.link, data.tags) }

            // open url with browser
            val openListener = View.OnClickListener { startWebBrowser(data.domain.url + data.link.url) }
            binding.previewImageView.setOnClickListener(openListener)
            binding.nameTextView.setOnClickListener(openListener)
            binding.fullUrlTextView.setOnClickListener(openListener)
        })

        // show image preview
        viewModel.imageUrl.observe(viewLifecycleOwner,
            {
                if (!it.isNullOrEmpty()) {
                    SjImageViewUtil.setImage(
                        fragment = this,
                        binding.previewImageView,
                        it,
                        R.drawable.ic_baseline_web_24
                    )
                } else {
                    SjImageViewUtil.setDefaultImage(
                        fragment = this,
                        binding.previewImageView,
                        R.drawable.ic_baseline_web_24
                    )
                }
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

    // handle user event methods
    private fun moveToPlayFragment() {
        val url = "https://www.youtube.com/watch?v=H0M1yU6uO30"
        moveToOtherFragment(PlayVideoFragment.newInstance(url))
        Toast.makeText(context, "pressed", Toast.LENGTH_LONG).show()
    }

    private fun deleteLink(link: SjLink, tags: List<SjTag>) {
        viewModel.deleteLink(link, tags)
        popBack()
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
package com.github.yeeun_yun97.toy.linksaver.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.palette.graphics.Palette
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLink
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentDetailLinkBinding
import com.github.yeeun_yun97.toy.linksaver.ui.activity.EditLinkActivity
import com.github.yeeun_yun97.toy.linksaver.ui.component.SjTagChip
import com.github.yeeun_yun97.toy.linksaver.ui.component.VideoPreloadWorker
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.DetailLinkViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        viewModel.loadLinkData(lid)
    }

    override fun onCreateView() {
        binding.viewModel = viewModel

        binding.toolbar.setMenu(
            R.menu.menu_link_datail,
            hashMapOf(R.id.playItem to ::moveToPlayFragment)
        )

        lid = requireArguments().getInt("lid")
        viewModel.tags.observe(viewLifecycleOwner, { tagList ->
            if (tagList.isEmpty()) {
                binding.tagEmptyGroup.visibility = View.VISIBLE
            } else {
                binding.tagEmptyGroup.visibility = View.GONE
            }
        })

        viewModel.link.observe(viewLifecycleOwner, { data ->
            val fullUrl = data.domain.url + data.link.url
            viewModel.loadPreviewImageUrl(fullUrl)

            binding.tagChipGroup.removeAllViews()
            for (tag in data.tags) {
                binding.tagChipGroup.addView(
                    SjTagChip(
                        requireContext(),
                        tag
                    ).apply { setViewMode() })
            }

            binding.editImageView.setOnClickListener { startEditActivityToUpdate(data.link.lid) }
            binding.deleteImageView.setOnClickListener { deleteLink(data.link, data.tags) }
            val openListener =
                View.OnClickListener { startWebBrowser(data.domain.url + data.link.url) }
            binding.previewImageView.setOnClickListener(openListener)
            binding.nameTextView.setOnClickListener(openListener)
            binding.fullUrlTextView.setOnClickListener(openListener)
        })

        viewModel.imageUrl.observe(viewLifecycleOwner,
            {
                if (!it.isNullOrEmpty()) {

                    CoroutineScope(Dispatchers.IO).launch {
                        val bitmap =
                            Glide.with(this@DetailLinkFragment).asBitmap().load(it).submit().get()
                        val palette = Palette.from(bitmap).generate()

                        launch(Dispatchers.Main) {
                            val swatch = palette.mutedSwatch ?: palette.darkMutedSwatch
                            if (swatch != null) binding.previewImageView.setBackgroundColor(swatch.rgb)
                            else binding.previewImageView.setBackgroundColor(Color.TRANSPARENT)
                        }
                    }
                    Log.d("imageResource", it)
                    Glide.with(this).load(it).centerCrop().into(binding.previewImageView)
                }
            })
        schedulePreloadWork("https://www.youtube.com/watch?v=H0M1yU6uO30")
    }

    private fun schedulePreloadWork(videoUrl: String) {
        val workManager = WorkManager.getInstance(requireActivity().applicationContext)
        val videoPreloadWorker = VideoPreloadWorker.buildWorkRequest(videoUrl)
        workManager.enqueueUniqueWork(
            "VideoPreloadWorker",
            ExistingWorkPolicy.KEEP,
            videoPreloadWorker
        )
    }

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
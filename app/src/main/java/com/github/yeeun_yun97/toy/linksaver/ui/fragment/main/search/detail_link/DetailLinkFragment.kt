package com.github.yeeun_yun97.toy.linksaver.ui.fragment.main.search.detail_link

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.github.yeeun_yun97.clone.ynmodule.ui.component.SjImageViewUtil
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.FullNameTagValue
import com.github.yeeun_yun97.toy.linksaver.data.model.LinkDetailValue
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentDetailLinkBinding
import com.github.yeeun_yun97.toy.linksaver.ui.activity.EditLinkActivity
import com.github.yeeun_yun97.toy.linksaver.ui.component.SjUtil
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.detail_link.DetailLinkViewModel

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

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView() {
        // get lid from argument
        lid = requireArguments().getInt("lid")

        // set variable of binding
        binding.viewModel = viewModel

        // set toolbar menu
        val handlerMap = hashMapOf<Int, () -> Unit>(
            R.id.menu_edit_link to ::startEditActivityToUpdate,
            R.id.menu_delete_link to ::deleteLink
        )
        binding.toolbar.setMenu(R.menu.toolbar_menu_detail_link, handlerMap)

        // set linkdata user to click event
        viewModel.link.observe(viewLifecycleOwner, { data ->
            showPreviewOfLink(data)
            showOrHideTagChipGroup(data.tags)
        })

        // set previewWebView
        binding.previewWebView.webViewClient =
            WebViewClient()              // prevent opening browser
        binding.previewWebView.settings.javaScriptEnabled =
            true            // show javascript needed pages

        // set open web by views
        val openListener =
            View.OnClickListener { startWebBrowser() }
        binding.nameTextView.setOnClickListener(openListener)
        binding.fullUrlTextView.setOnClickListener(openListener)
        binding.previewImageView.setOnClickListener(openListener)
        binding.previewWebView.setOnTouchListener { view, event ->
            startWebBrowser()
            true
        }   // prevent touch, view-only webView
    }

    private fun showOrHideTagChipGroup(tagList: List<SjTag>) {
        if (tagList.isEmpty()) {
            binding.tagEmptyGroup.visibility = View.VISIBLE
        } else {
            binding.tagEmptyGroup.visibility = View.GONE
        }
    }

    private fun showPreviewOfLink(data: LinkDetailValue) {
        // show preview
        if (data.isVideo && !data.isYoutubeVideo) {
            // case simple video
            binding.previewImageView.visibility = View.VISIBLE
            binding.previewWebView.visibility = View.GONE
            Glide.with(requireContext())
                .load(data.fullUrl)
                .centerCrop()
                .override(720, 360)
                .into(binding.previewImageView)
        } else {
            if (data.preview.isNotEmpty()) {
                // case has preview image
                binding.previewImageView.visibility = View.VISIBLE
                binding.previewWebView.visibility = View.GONE
                SjImageViewUtil.setImage(
                    fragment = this,
                    binding.previewImageView,
                    data.preview,
                    R.drawable.ic_icons8_no_image_100
                )
            } else {
                // case has no preview image, show webView
                val fullUrl = viewModel.bindingFullUrl.value ?: ""
                if (SjUtil.checkUrlPrefix(fullUrl)) {
                    binding.previewWebView.visibility = View.VISIBLE
                    binding.previewImageView.visibility = View.GONE
                    binding.previewWebView.loadUrl(fullUrl)
                }
            }
        }
    }

    private fun deleteLink() {
        viewModel.deleteLink()
        popBack()
    }

    private fun startEditActivityToUpdate() {
        val linkData = viewModel.link.value
        if (linkData is LinkDetailValue) {
            val intent = Intent(requireContext(), EditLinkActivity::class.java)
            intent.putExtra("lid", linkData.lid)
            startActivity(intent)
        } else {
            Log.e("cannot start EditActivity", "cause: link data is null")
        }

    }

    private fun startWebBrowser() {
        val url = viewModel.link.value?.fullUrl
        if (url is String) {
            if (SjUtil.checkUrlPrefix(url)) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            } else {
                // string url is wrong
            }
        }

    }
}
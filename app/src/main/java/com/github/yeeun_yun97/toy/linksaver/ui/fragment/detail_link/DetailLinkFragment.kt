package com.github.yeeun_yun97.toy.linksaver.ui.fragment.detail_link

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.github.yeeun_yun97.clone.ynmodule.ui.component.SjImageViewUtil
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.data.model.LinkModelUtil
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLink
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

    override fun onCreateView() {
        // get lid from argument
        lid = requireArguments().getInt("lid")

        // set variable of binding
        binding.viewModel = viewModel

        // show tags or show empty view
        viewModel.bindingTags.observe(viewLifecycleOwner, { tagList ->
            if (tagList.isEmpty()) {
                binding.tagEmptyGroup.visibility = View.VISIBLE
            } else {
                binding.tagEmptyGroup.visibility = View.GONE
            }
        })

        // set linkdata user to click event
        viewModel.link.observe(viewLifecycleOwner, { data ->
            // edit
            binding.editImageView.setOnClickListener { startEditActivityToUpdate(data.link.lid) }

            // delete
            binding.deleteImageView.setOnClickListener { deleteLink(data.link, data.tags) }

            // open url with browser
            val openListener = View.OnClickListener { startWebBrowser(LinkModelUtil.getFullUrl(data)) }
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
                        R.drawable.ic_icons8_no_image_100
                    )
                } else {
                    SjImageViewUtil.setDefaultImage(
                        fragment = this,
                        binding.previewImageView,
                        R.drawable.ic_icons8_no_image_100
                    )
                }
            })
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

    private fun startWebBrowser(url: String) {
        if (SjUtil.checkUrlPrefix(url)) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }else{
            // string url is wrong
        }
    }
}
package com.github.yeeun_yun97.toy.linksaver.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.databinding.FragmentEditVideoBinding
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic.SjBasicFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.EditVideoViewModel

class EditVideoFragment : SjBasicFragment<FragmentEditVideoBinding>() {

    val viewModel:EditVideoViewModel by activityViewModels()

    companion object{
        fun newInstance(url:String):EditVideoFragment{
            val fragment = EditVideoFragment()
            fragment.arguments= Bundle().apply { putString("url",url) }
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_edit_video

    override fun onCreateView() {
        binding.viewModel = viewModel

        val arguments = requireArguments()
        val url = arguments.getString("url") ?: ""

        viewModel.loadUrl(url)





        val handlerMap = hashMapOf<Int, () -> Unit>(
            R.id.menu_save to ::saveVideo
        )
//        binding.toolbar.setMenu(R.menu.toolbar_menu_edit, handlerMap)

    }

    private fun saveVideo() {

    }
}
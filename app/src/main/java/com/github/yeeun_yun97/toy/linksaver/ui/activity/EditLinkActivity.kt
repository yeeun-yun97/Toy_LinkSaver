package com.github.yeeun_yun97.toy.linksaver.ui.activity

import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.github.yeeun_yun97.toy.linksaver.databinding.ActivityPlainBinding
import com.github.yeeun_yun97.toy.linksaver.ui.activity.basic.SjBasicActivity
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.edit_link.EditLinkFragment
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.edit_link.EditLinkPasteFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.EditLinkViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditLinkActivity : SjBasicActivity<ActivityPlainBinding>() {
    private val editViewModel: EditLinkViewModel by viewModels()

    @Inject lateinit var editPasteFragment : EditLinkPasteFragment
    @Inject lateinit var editFragment : EditLinkFragment

    override fun viewBindingInflate(inflater: LayoutInflater): ActivityPlainBinding =
        ActivityPlainBinding.inflate(layoutInflater)

    override fun homeFragment(): Fragment {
        val lid = intent.getIntExtra("lid", -1)
        val url = intent.getStringExtra("url") ?: ""
        when {
            lid != -1 -> editViewModel.setLinkByLid(lid)
            url.isNotEmpty() -> editViewModel.createLinkByUrl(url)
            else -> return editPasteFragment
        }
        return editFragment
    }

    override fun onCreate() {}


}
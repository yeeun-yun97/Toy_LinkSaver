package com.github.yeeun_yun97.toy.linksaver.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.databinding.ActivityEditLinkBinding
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.EditLinkFragment
import com.github.yeeun_yun97.toy.linksaver.viewmodel.EditLinkViewModel


class EditLinkActivity : AppCompatActivity() {
    private val viewModel: EditLinkViewModel by viewModels()
    private lateinit var binding : ActivityEditLinkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //view binding
        binding = ActivityEditLinkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.commit {
            add(R.id.activityRoot, EditLinkFragment())
            setReorderingAllowed(true)
            //addToBackStack("editLink")
        }
    }

}
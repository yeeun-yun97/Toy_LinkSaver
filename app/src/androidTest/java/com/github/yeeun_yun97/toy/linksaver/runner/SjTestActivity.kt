package com.github.yeeun_yun97.toy.linksaver.runner

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.github.yeeun_yun97.toy.linksaver.viewmodel.link.ListVideoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SjTestActivity : AppCompatActivity() {
    val listVideoViewModel : ListVideoViewModel by viewModels()


}
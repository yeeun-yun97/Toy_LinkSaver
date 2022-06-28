package com.github.yeeun_yun97.toy.linksaver.data.repository.room

import com.github.yeeun_yun97.toy.linksaver.viewmodel.search.SearchLinkViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val vmModule = module(createdAtStart = true) {
        viewModel { SearchLinkViewModel() }
}
package com.github.yeeun_yun97.clone.ynmodule.ui.component

import android.view.View

enum class DataState {
    LOADING, EMPTY, LOADED;
}

class ViewVisibilityUtil(
    private val loadingView: View,
    private val emptyView: View,
    private val loadedView: View
) {
    var state: DataState = DataState.LOADING
        set(value) {
            when (value) {
                DataState.LOADING -> {
                    loadingView.visibility = View.VISIBLE
                    emptyView.visibility = View.GONE
                    loadedView.visibility = View.GONE
                }
                DataState.EMPTY -> {
                    loadingView.visibility = View.GONE
                    emptyView.visibility = View.VISIBLE
                    loadedView.visibility = View.GONE
                }
                DataState.LOADED -> {
                    loadingView.visibility = View.GONE
                    emptyView.visibility = View.GONE
                    loadedView.visibility = View.VISIBLE
                }
            }
            field = value
        }

    init {
        state = DataState.LOADING
    }


}
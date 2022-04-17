package com.github.yeeun_yun97.toy.linksaver.ui.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.yeeun_yun97.toy.linksaver.*
import com.github.yeeun_yun97.toy.linksaver.ui.adapter.LinksAdapter
import com.github.yeeun_yun97.toy.linksaver.viewmodel.ViewLinkViewModel

class ViewLinkActivity : AppCompatActivity() {
    private val recyclerView: RecyclerView by lazy { findViewById(R.id.ViewLinkActivity_recyclerView) }
    private val viewModel: ViewLinkViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_link)
        viewModel.loadDatas()
        val adapter = LinksAdapter(viewModel.linkList, ::startWebBrowser)
        recyclerView.adapter = adapter;
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    fun startWebBrowser(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}
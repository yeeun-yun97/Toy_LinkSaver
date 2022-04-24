package com.github.yeeun_yun97.toy.linksaver.ui.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.yeeun_yun97.toy.linksaver.*
import com.github.yeeun_yun97.toy.linksaver.ui.adapter.LinksAdapter
import com.github.yeeun_yun97.toy.linksaver.viewmodel.ViewLinkViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ViewLinkActivity : AppCompatActivity() {
    private val recyclerView: RecyclerView by lazy { findViewById(R.id.ViewLinkActivity_recyclerView) }
    private val floatingButton: FloatingActionButton by lazy { findViewById(R.id.ViewLinkActivity_floatingActionButton) }

    private val viewModel: ViewLinkViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_link)

        val adapter = LinksAdapter(::startWebBrowser)
        viewModel.linkList.observe(this,
            Observer {
                adapter.itemList = it
                adapter.notifyDataSetChanged()
            }
        )

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        floatingButton.setOnClickListener { startEditActivity() }
    }


    override fun onResume() {
        super.onResume()
        viewModel.loadDatas()
    }

    private fun startEditActivity() {
        startActivity(Intent(this, EditLinkActivity::class.java))
    }

    fun startWebBrowser(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}
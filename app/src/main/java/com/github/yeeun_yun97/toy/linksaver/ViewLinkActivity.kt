package com.github.yeeun_yun97.toy.linksaver

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ViewLinkActivity : AppCompatActivity() {
    private val recyclerView: RecyclerView by lazy { findViewById(R.id.ViewLinkActivity_recyclerView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_link)
        val links = createDatas()
        val adapter = LinksAdapter(links, ::startWebBrowser)
        recyclerView.adapter = adapter;
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun createDatas(): ArrayList<SjLink> {
        var youtube = SjDomain("유튜브", "https://m.youtube.com/watch?")
        var allchan = SjDomain("올찬식탁", "https://m.smartstore.naver.com/allchanfood/products/")

        var bread = SjTag("빵")

        var pokemonBread = SjLink("돌아온 포켓몬빵", allchan, "6333541088", bread)
        var pieceCake = SjLink("조각 케이크", allchan, "718907248", bread)
        var ytnNews = SjLink("포켓몬빵 뉴스", youtube, "v=GJ3a33gdx7o", bread)

        val links = ArrayList<SjLink>().apply {
            add(pokemonBread)
            add(pieceCake)
            add(ytnNews)
        }
        return links
    }

    fun startWebBrowser(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}
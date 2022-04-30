package com.github.yeeun_yun97.toy.linksaver.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup

class SjNetworkRepository private constructor() {

    //singleton
    companion object {
        lateinit var networkRepository: SjNetworkRepository
        fun newInstance(): SjNetworkRepository {
            if (!this::networkRepository.isInitialized) {
                networkRepository = SjNetworkRepository()
            }
            return networkRepository
        }
    }

    private val client = OkHttpClient()
    private val _siteTitle = MutableLiveData<String>()
    val siteTitle: MutableLiveData<String> get() = _siteTitle


    fun getTitleOf(url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("loadTitle", "start")

            val request = Request.Builder()
                .url(url)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) Log.e("Error", "UnExpected code $response")
                val html = Jsoup.parse(response.body!!.string())
                Log.d("loadTitle", html.title())
                _siteTitle.postValue(html.title())
            }
        }
    }


}
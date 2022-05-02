package com.github.yeeun_yun97.toy.linksaver.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup

class SjNetworkRepository private constructor() {
    private val client = OkHttpClient()
    private val _siteTitle = MutableLiveData<String>()
    val siteTitle: LiveData<String> get() = _siteTitle

    companion object {
        //singleton object
        lateinit var networkRepository: SjNetworkRepository

        fun newInstance(): SjNetworkRepository {
            if (!this::networkRepository.isInitialized) {
                networkRepository = SjNetworkRepository()
            }
            return networkRepository
        }

    }

    fun getTitleOf(url: String) {
        if (url.isEmpty() || !url.startsWith("http")) return
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val request = Request.Builder().url(url).build()
                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) Log.e("Error", "UnExpected code $response")
                    val html = Jsoup.parse(response.body!!.string())
                    Log.d("loadTitle", html.title())
                    _siteTitle.postValue(html.title())
                }
            } catch (e: Exception) {
                /*
                유저가 입력한 url이 완벽하지 않아서 오류가 날 수도 있다.
                근데 이건 오류가 아니고, 아직 입력중인 것일 수도 있는 심각하지 않은 문제다.
                 */
            }
        }
    }


}
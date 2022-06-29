package com.github.yeeun_yun97.toy.linksaver.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.delay
import java.util.concurrent.TimeoutException


suspend fun <T>getValueOrTimeOut(liveData: LiveData<T>, timeout:Long = 2000): T {
    var value: T? = null
    liveData.observeForever{ value = it }

    // wait
    delay(timeout)
    Log.d("Waiting","start")

    // return value or throw
    if(value != null){
        throw TimeoutException("LiveData has Null Value")
    }else {
        return value!!
    }
}
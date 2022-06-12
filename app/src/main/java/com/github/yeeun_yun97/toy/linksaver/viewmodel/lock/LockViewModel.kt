package com.github.yeeun_yun97.toy.linksaver.viewmodel.lock

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LockViewModel : ViewModel() {

    private val _bindingPassword1 = MutableLiveData("")
    private val _bindingPassword2 = MutableLiveData("")
    private val _bindingPassword3 = MutableLiveData("")
    private val _bindingPassword4 = MutableLiveData("")
    private val _bindingPassword5 = MutableLiveData("")
    private val _bindingPassword6 = MutableLiveData("")

    val bindingPassword1: LiveData<String> get() = _bindingPassword1
    val bindingPassword2: LiveData<String> get() = _bindingPassword2
    val bindingPassword3: LiveData<String> get() = _bindingPassword3
    val bindingPassword4: LiveData<String> get() = _bindingPassword4
    val bindingPassword5: LiveData<String> get() = _bindingPassword5
    val bindingPassword6: LiveData<String> get() = _bindingPassword6

    private var length = 0
    private val passwordBuilder = StringBuilder(6)

    init {
        bindingPassword6.observeForever {
            if (!it.isNullOrEmpty() && length == 6) {
                Log.d("password completed", passwordBuilder.toString())
                clearNumbers()
            }
        }
    }

    private fun getLiveData(): MutableLiveData<String> {
        return when (length) {
            0 -> _bindingPassword1
            1 -> _bindingPassword2
            2 -> _bindingPassword3
            3 -> _bindingPassword4
            4 -> _bindingPassword5
            5 -> _bindingPassword6
            else -> throw Exception()
        }
    }

    fun appendNumber(value: Int) {
        val stringVal = value.toString()
        getLiveData().postValue(stringVal)
        passwordBuilder.append(stringVal)
        length++
    }

    fun clearNumbers() {
        passwordBuilder.setLength(0)
        length = 0
        _bindingPassword1.value = ""
        _bindingPassword2.value = ""
        _bindingPassword3.value = ""
        _bindingPassword4.value = ""
        _bindingPassword5.value = ""
        _bindingPassword6.value = ""
    }


}
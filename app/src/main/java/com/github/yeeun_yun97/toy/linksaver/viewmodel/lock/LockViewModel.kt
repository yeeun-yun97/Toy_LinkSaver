package com.github.yeeun_yun97.toy.linksaver.viewmodel.lock

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.yeeun_yun97.toy.linksaver.data.repository.SjDataStoreRepository
import com.github.yeeun_yun97.toy.linksaver.viewmodel.base.SjBaseAndroidViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LockViewModel @Inject constructor(
    application: Application,
    private val repository: SjDataStoreRepository
) : SjBaseAndroidViewModelImpl(application) {
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

    private val _isPasswordCorrect = MutableLiveData<Boolean>()
    val isPasswordCorrect: LiveData<Boolean> get() = _isPasswordCorrect
    private val _password = MutableLiveData<String>()
    val password: LiveData<String> get() = _password

    private var length = 0
    private val passwordBuilder = StringBuilder(6)
    private var wrongCount = 0

    init {
        bindingPassword6.observeForever {
            if (!it.isNullOrEmpty() && length == 6) {
                viewModelScope.launch(Dispatchers.IO) {
                    val expected = repository.getPassword(application.applicationContext).first()
                    val actual = passwordBuilder.toString()
                    checkPassword(expected, actual)
                    clearNumbers()
                }
            }
        }
    }

    override fun refreshData() {}

    private fun checkPassword(expected: String, actual: String) {
        _password.postValue(actual)
        if (expected.isNotEmpty()&&Integer.parseInt(expected) == Integer.parseInt(actual)) {
            _isPasswordCorrect.postValue(true)
        } else {
            _isPasswordCorrect.postValue(false)
            wrongCount++
        }
    }

    fun setPassword(password: String) {
        repository.setPassword(getApplication<Application>().applicationContext, password)
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
        _bindingPassword1.postValue("")
        _bindingPassword2.postValue("")
        _bindingPassword3.postValue("")
        _bindingPassword4.postValue("")
        _bindingPassword5.postValue("")
        _bindingPassword6.postValue("")
    }




}
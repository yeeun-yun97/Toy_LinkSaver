package com.github.yeeun_yun97.clone.ynmodule.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class DataBindingBasicFragment<T : ViewDataBinding> : Fragment() {
    private var _binding: T? = null
    protected val binding: T get() = _binding!!

    // override methods
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutId(), container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        onCreateView()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Fragment의 수명주기는 뷰가 없어진 후에도 존재하므로,
        // 뷰가 없어질 때 binding과의 연결을 끊는 것이 자원 낭비를 방지한다.
        _binding = null
    }


    // Log TAG로 쓰거나 할 때 사용할 method: 편의
    protected fun getClassName(): String = this::javaClass.name

    // abstract methods
    /** Fragment의 layout ID를 반환한다. */
    protected abstract fun layoutId(): Int

    /** onCreate()에서 할 일이 있다면 여기서. */
    protected abstract fun onCreateView()


}
package com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.github.yeeun_yun97.toy.linksaver.R
import com.github.yeeun_yun97.toy.linksaver.ui.fragment.EditDomainFragment

abstract class DataBindingBasicFragment<T : ViewDataBinding> : Fragment() {

    private var _binding: T? = null
    protected val binding: T get() = _binding!!
    protected abstract fun layoutId(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutId(), container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getClassName(): String{
        return this.javaClass.canonicalName
    }

    fun moveToOtherFragment(fragment:Fragment){
        parentFragmentManager.commit{
            replace(R.id.fragmentContainer, fragment)
            setReorderingAllowed(true)
            addToBackStack("editTag")
        }
    }

}
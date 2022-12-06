package com.top1shvetsvadim1.coreutils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.dolatkia.animatedThemeManager.ThemeFragment

abstract class BaseThemeFragment<VB : ViewBinding> : ThemeFragment() {
    private var _binding: VB? = null
    val binding: VB
        get() = _binding as VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater)
        return binding.root
    }

    fun navigateTo(direction: NavDirections) {
        tryNull {
            findNavController().navigate(direction)
        }
    }

    abstract fun getViewBinding(inflater: LayoutInflater): VB


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
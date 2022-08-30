package com.top1shvetsvadim1.coreutils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseFragment<S : ViewState, E : ViewEvent, VM : BaseViewModel<*, S, E>, VB : ViewBinding> :
    Fragment() {
    private var _binding: VB? = null
    val binding: VB
        get() = _binding as VB

    abstract val viewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                withContext(Dispatchers.Main) {
                    viewModel.reducer.state.collectLatest(this@BaseFragment::render)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.reducer.effects.collectLatest(this@BaseFragment::handleEffect)
        }
        setupViews()
    }

    abstract fun setupViews()

    abstract fun render(state: S)
    open fun handleEffect(effect: E) {}

    abstract fun getViewBinding(inflater: LayoutInflater): VB

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
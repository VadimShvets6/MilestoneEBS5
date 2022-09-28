package com.top1shvetsvadim1.coreutils

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseFragment<S : ViewState, E : ViewEvent, VM : BaseViewModel<*, S, E>, VB : ViewBinding> : Fragment() {

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

    fun navigateTo(direction: NavDirections) {
        findNavController().navigate(direction)
        tryNull {

        }
    }

    fun createSnackBar(text: String, @ColorRes background: Int, @ColorRes textColor: Int) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(requireActivity(), background))
            .setTextColor(ContextCompat.getColor(requireActivity(), textColor))
            .show()
    }

    fun popBack() {
        tryNull {
            findNavController().popBackStack()
        }
    }

    abstract fun render(state: S)
    open fun handleEffect(effect: E) {}

    abstract fun getViewBinding(inflater: LayoutInflater): VB

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    inline fun <reified T> getContextSafe(crossinline action: Context.() -> T): T? {
        return context?.let(action)
    }
}

sealed interface GeneralEvent : ViewEvent {
    object GeneralException : GeneralEvent
    object ShowNoInternet : GeneralEvent
}
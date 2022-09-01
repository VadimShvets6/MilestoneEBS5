package com.top1shvetsvadim1.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.top1shvetsvadim1.coreui.Colors
import com.top1shvetsvadim1.coreutils.Action
import com.top1shvetsvadim1.coreutils.BaseAdapter
import com.top1shvetsvadim1.coreutils.BaseFragment
import com.top1shvetsvadim1.presentation.delegate.ProductDelegate
import com.top1shvetsvadim1.presentation.databinding.FragmentMainBinding
import com.top1shvetsvadim1.presentation.mvi.MainEvent
import com.top1shvetsvadim1.presentation.mvi.MainIntent
import com.top1shvetsvadim1.presentation.mvi.MainState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : BaseFragment<MainState, MainEvent, MainViewModel, FragmentMainBinding>() {

    override val viewModel: MainViewModel by viewModels()

    private val productAdapter by BaseAdapter.Builder()
        .setDelegates(ProductDelegate())
        .setActionProcessor(::onProductClicked)
        .buildIn()

    private fun onProductClicked(action: Action) {
        when (action) {
            is ProductDelegate.ActionProductAdapter.OnProductClicked -> findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToDetailFragment(action.productItem.id)
            )
            is ProductDelegate.ActionProductAdapter.OnProductFavoriteClicked ->
                if (action.setFavorite) {
                    viewModel.handleAction(MainIntent.AddItemToFavorite(action.id))
                } else {
                    viewModel.handleAction(MainIntent.RemoveItemFromFavorite(action.id))
                }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.handleAction(MainIntent.LoadItems)
    }

    override fun getViewBinding(inflater: LayoutInflater): FragmentMainBinding {
        return FragmentMainBinding.inflate(inflater)
    }

    override fun setupViews() {
        binding.progressBar.isVisible = false
        binding.rvProductList.adapter = productAdapter
    }

    //TODO: move it to your base fragment. Note: you can dynamically create views in any ViewGroup and change their layout params.
    override fun handleEffect(effect: MainEvent) {
        when (effect) {
            MainEvent.GeneralException -> {
                binding.button.isVisible = false
                binding.tvError.isVisible = true
                binding.progressBar.isVisible = false
            }
            MainEvent.ShowNoInternet -> {
                binding.button.isVisible = false
                Snackbar.make(binding.root, "No internet connection", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(
                        ContextCompat.getColor(
                            requireActivity(),
                            Colors.color_main_07195C
                        )
                    )
                    .setTextColor(ContextCompat.getColor(requireActivity(), Colors.white))
                    .show()
                lifecycleScope.launch(Dispatchers.Main) {
                    //TODO: strange delay. You should show try again option immediately, if you app is not cache-first.
                    delay(10000)
                    binding.progressBar.isVisible = false
                    binding.buttonTryAgain.apply {
                        isVisible = true
                        setOnClickListener {
                            viewModel.handleAction(MainIntent.LoadItems)
                            binding.buttonTryAgain.isVisible = false
                            binding.progressBar.isVisible = true
                        }
                    }
                }
            }
        }
    }

    override fun render(state: MainState) {
        binding.progressBar.isVisible = state.isLoading
        lifecycleScope.launchWhenResumed {
            state.items.collectLatest {
                productAdapter.submitList(it)
            }
        }
        binding.toolbar.setClickOnRightImage {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToFavoriteFragment())
        }
    }
}
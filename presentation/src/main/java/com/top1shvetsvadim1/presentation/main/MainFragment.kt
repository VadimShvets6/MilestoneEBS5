package com.top1shvetsvadim1.presentation.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.top1shvetsvadim1.coreui.Colors
import com.top1shvetsvadim1.coreutils.Action
import com.top1shvetsvadim1.coreutils.BaseAdapter
import com.top1shvetsvadim1.coreutils.BaseFragment
import com.top1shvetsvadim1.domain.models.Filters
import com.top1shvetsvadim1.presentation.customView.BottomSheetFilter
import com.top1shvetsvadim1.presentation.databinding.FragmentMainBinding
import com.top1shvetsvadim1.presentation.delegate.ProductDelegate
import com.top1shvetsvadim1.presentation.mvi.MainEvent
import com.top1shvetsvadim1.presentation.mvi.MainIntent
import com.top1shvetsvadim1.presentation.mvi.MainState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : BaseFragment<MainState, MainEvent, MainViewModel, FragmentMainBinding>() {

    override val viewModel: MainViewModel by viewModels()

    private val productAdapter by BaseAdapter.Builder()
        .setDelegates(ProductDelegate())
        .setActionProcessor(::onProductClicked)
        .buildIn()

    private val bottomSheet by lazy {
        BottomSheetFilter(requireContext(), com.top1shvetsvadim1.coreui.R.style.BottomSheetDialogTheme) {
            when (it) {
                Filters.PRICE -> viewModel.handleAction(MainIntent.LoadItems(it.name.lowercase()))
                Filters.SIZE -> viewModel.handleAction(MainIntent.LoadItems(it.name.lowercase()))
                Filters.COLOR -> viewModel.handleAction(MainIntent.LoadItems(it.name.lowercase()))
                Filters.RESET -> viewModel.handleAction(MainIntent.LoadItems())
            }
        }
    }

    private fun onProductClicked(action: Action) {
        when (action) {
            is ProductDelegate.ActionProductAdapter.OnProductClicked -> {
                Log.d("Test", action.productItem.name)
                navigateTo(
                    MainFragmentDirections.actionMainFragmentToDetailFragment(action.productItem.id)
                )
            }
            is ProductDelegate.ActionProductAdapter.OnProductCartClicked -> {
                if (action.setCart) {
                    viewModel.handleAction(MainIntent.AddItemToCart(action.id))
                } else {
                    viewModel.handleAction(MainIntent.RemoveItemFromCart(action.id))
                }
            }
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
        Log.d("Test", "Create")
        viewModel.handleAction(MainIntent.LoadItems())
    }

    override fun getViewBinding(inflater: LayoutInflater): FragmentMainBinding {
        return FragmentMainBinding.inflate(inflater)
    }

    override fun setupViews() {
        binding.progressBar.isVisible = false
        binding.rvProductList.adapter = productAdapter
    }

    override fun handleEffect(effect: MainEvent) {
        super.handleEffect(effect)
        when (effect) {
            MainEvent.GeneralException -> {
                binding.buttonCart.isVisible = false
                binding.tvError.isVisible = true
                binding.progressBar.isVisible = false
            }
            MainEvent.ShowNoInternet -> {
                binding.buttonCart.isVisible = false
                createSnackBar("No internet connection", Colors.color_main_07195C, Colors.white)
                lifecycleScope.launch(Dispatchers.Main) {
                    binding.progressBar.isVisible = false
                    binding.buttonTryAgain.apply {
                        isVisible = true
                        setOnClickListener {
                            viewModel.handleAction(MainIntent.LoadItems())
                            binding.buttonTryAgain.isVisible = false
                            binding.progressBar.isVisible = true
                        }
                    }
                }
            }
        }
    }

    private var job: Job? = null

    override fun render(state: MainState) {
        binding.progressBar.isVisible = state.isLoading
        job?.cancel()
        job = lifecycleScope.launchWhenResumed {
            state.item.collectLatest {
                Log.d("List", "$it")
                productAdapter.submitList(it)
            }
            bottomSheet.setList(state.itemsFilter)
        }
        binding.toolbar.setClickOnRightImage {
            navigateTo(MainFragmentDirections.actionMainFragmentToFavoriteFragment())
        }
        binding.toolbar.setClickOnLeftImage {
            navigateTo(MainFragmentDirections.actionMainFragmentToProfileFragment())
        }
        binding.buttonCart.setOnClickListener {
            navigateTo(MainFragmentDirections.actionMainFragmentToCartFragment())
        }
        binding.filters.setOnClickListener {
            bottomSheet.showDialog()
        }
    }
}
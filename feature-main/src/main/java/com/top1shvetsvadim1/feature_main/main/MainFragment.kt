package com.top1shvetsvadim1.feature_main.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.top1shvetsvadim1.coreui.Colors
import com.top1shvetsvadim1.coreutils.*
import com.top1shvetsvadim1.domain.models.Filters
import com.top1shvetsvadim1.feature_main.bottomSheets.BottomSheetFilter
import com.top1shvetsvadim1.feature_main.databinding.FragmentMainBinding
import com.top1shvetsvadim1.feature_main.delegate.ProductDelegate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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

    private val bottomSheet by lazy {
        BottomSheetFilter(requireContext()) {
            when (it) {
                Filters.PRICE -> {
                    viewModel.handleAction(MainIntent.LoadItems(Filters.PRICE))
                    scrollToTop()
                }
                Filters.SIZE -> {
                    viewModel.handleAction(MainIntent.LoadItems(Filters.SIZE))
                    scrollToTop()
                }
                Filters.COLOR -> {
                    viewModel.handleAction(MainIntent.LoadItems(Filters.COLOR))
                    scrollToTop()
                }
                Filters.RESET -> {
                    viewModel.handleAction(MainIntent.LoadItems(Filters.RESET))
                    scrollToTop()
                }
            }
        }
    }

    private fun scrollToTop() {
        lifecycleScope.launch(Dispatchers.Main) {
            delay(300)
            binding.rvProductList.smoothScrollToPosition(0)
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
            bottomSheet.show()
        }
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
        binding.buttonCart.setRightImage(state.cartSize)
        job?.cancel()
        job = lifecycleScope.launchWhenCreated{
            state.item.collectLatest {
                Log.d("List", "$it")
                productAdapter.submitList(it)
            }
            bottomSheet.setList(state.itemsFilter)
        }
    }
}
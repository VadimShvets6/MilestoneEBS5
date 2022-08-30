package com.top1shvetsvadim1.presentation.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.top1shvetsvadim1.coreui.Drawable
import com.top1shvetsvadim1.coreutils.Action
import com.top1shvetsvadim1.coreutils.BaseAdapter
import com.top1shvetsvadim1.coreutils.BaseFragment
import com.top1shvetsvadim1.presentation.databinding.FragmentFavoriteBinding
import com.top1shvetsvadim1.presentation.delegate.ProductDelegate
import com.top1shvetsvadim1.presentation.mvi.FavoriteEvent
import com.top1shvetsvadim1.presentation.mvi.FavoriteIntent
import com.top1shvetsvadim1.presentation.mvi.FavoriteState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class FavoriteFragment :
    BaseFragment<FavoriteState, FavoriteEvent, FavoriteViewModel, FragmentFavoriteBinding>() {

    override val viewModel: FavoriteViewModel by viewModels()

    private val favoriteAdapter by BaseAdapter.Builder()
        .setDelegates(ProductDelegate())
        .setActionProcessor(::onItemClick)
        .buildIn()


    private fun onItemClick(action: Action) {
        when (action) {
            is ProductDelegate.ActionProductAdapter.OnProductClicked -> findNavController().navigate(
                FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(action.productItem.id)
            )
            is ProductDelegate.ActionProductAdapter.OnProductFavoriteClicked -> viewModel.handleAction(
                FavoriteIntent.RemoveItemFromFavorite(action.id)
            )
        }
    }


    override fun setupViews() {
        binding.rvListFavorite.adapter = favoriteAdapter
        binding.progressBar.isVisible = true
        binding.toolbar.setRightImage(Drawable.ic_heart_favorites)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.handleAction(FavoriteIntent.LoadItems)
    }

    override fun render(state: FavoriteState) {
        binding.progressBar.isVisible = state.isLoading
        lifecycleScope.launchWhenResumed {
            state.items.collectLatest {
                binding.tvListSize.text = "${it.size}"
                favoriteAdapter.submitList(it)
            }
        }
        binding.toolbar.setClickOnLeftImage {
            findNavController().popBackStack()
        }
    }

    override fun getViewBinding(inflater: LayoutInflater): FragmentFavoriteBinding {
        return FragmentFavoriteBinding.inflate(inflater)
    }
}
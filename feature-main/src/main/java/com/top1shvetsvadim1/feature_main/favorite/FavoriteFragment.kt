package com.top1shvetsvadim1.feature_main.favorite

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.top1shvetsvadim1.coreui.Drawable
import com.top1shvetsvadim1.coreutils.Action
import com.top1shvetsvadim1.coreutils.BaseAdapter
import com.top1shvetsvadim1.coreutils.BaseFragment
import com.top1shvetsvadim1.domain.models.Filters
import com.top1shvetsvadim1.feature_main.bottomSheets.BottomSheetFilter
import com.top1shvetsvadim1.feature_main.databinding.FragmentFavoriteBinding
import com.top1shvetsvadim1.feature_main.delegate.ProductDelegate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
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
            is ProductDelegate.ActionProductAdapter.OnProductClicked -> navigateTo(
                FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(action.productItem.id)
            )
            is ProductDelegate.ActionProductAdapter.OnProductFavoriteClicked -> viewModel.handleAction(
                FavoriteIntent.RemoveItemFromFavorite(action.id)
            )
        }
    }

    private val bottomSheet by lazy {
        BottomSheetFilter(requireContext()) {
            when (it) {
                Filters.PRICE -> viewModel.handleAction(FavoriteIntent.LoadItems(Filters.PRICE))
                Filters.SIZE -> viewModel.handleAction(FavoriteIntent.LoadItems())
                Filters.COLOR -> viewModel.handleAction(FavoriteIntent.LoadItems())
                Filters.RESET -> viewModel.handleAction(FavoriteIntent.LoadItems())
            }
        }
    }

    override fun setupViews() {
        binding.rvListFavorite.adapter = favoriteAdapter
        binding.toolbar.setRightImage(Drawable.ic_heart_favorites)
        binding.toolbar.setClickOnLeftImage {
            popBack()
        }
        binding.tvSortBy.setOnClickListener {
            bottomSheet.show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.handleAction(FavoriteIntent.LoadItems())
    }

    private var job: Job? = null

    override fun render(state: FavoriteState) {
        binding.progressBar.isVisible = state.isLoading
        job?.cancel()
        job = lifecycleScope.launchWhenResumed {
            bottomSheet.setList(state.itemsFilter)
            state.items.collectLatest {
                binding.tvListSize.text = "${it.size}"
                favoriteAdapter.submitList(it)
            }
        }
    }

    override fun getViewBinding(inflater: LayoutInflater): FragmentFavoriteBinding {
        return FragmentFavoriteBinding.inflate(inflater)
    }
}
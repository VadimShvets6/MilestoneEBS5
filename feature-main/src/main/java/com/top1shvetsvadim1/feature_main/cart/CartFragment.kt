package com.top1shvetsvadim1.feature_main.cart

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.top1shvetsvadim1.coreutils.Action
import com.top1shvetsvadim1.coreutils.BaseAdapter
import com.top1shvetsvadim1.coreutils.BaseFragment
import com.top1shvetsvadim1.domain.models.Filters
import com.top1shvetsvadim1.feature_main.bottomSheets.BottomSheetFilter
import com.top1shvetsvadim1.feature_main.databinding.FragmentCartBinding
import com.top1shvetsvadim1.feature_main.delegate.ProductCartDelegate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class CartFragment : BaseFragment<CartState, CartEvent, CartViewModel, FragmentCartBinding>() {

    override val viewModel: CartViewModel by viewModels()

    private val cartAdapter by BaseAdapter.Builder()
        .setDelegates(ProductCartDelegate())
        .setActionProcessor(::onProductClick)
        .buildIn()

    private fun onProductClick(action: Action) {
        when (action) {
            is ProductCartDelegate.ActionProductCartAdapter.OnProductDeleteClicked ->
                viewModel.handleAction(CartIntent.RemoveItemFromCart(action.id))
        }
    }

    private val bottomSheet by lazy {
        BottomSheetFilter(requireContext()) {
            when (it) {
                Filters.PRICE -> viewModel.handleAction(CartIntent.LoadItem(Filters.PRICE))
                Filters.SIZE -> viewModel.handleAction(CartIntent.LoadItem())
                Filters.COLOR -> viewModel.handleAction(CartIntent.LoadItem())
                Filters.RESET -> viewModel.handleAction(CartIntent.LoadItem())
            }
        }
    }

    override fun setupViews() {
        binding.progressBar.isVisible = false
        binding.rvListCart.adapter = cartAdapter
        binding.rvListCart.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 120) {
                    binding.expandableLayout0.collapse()
                }
                if (dy < -10) {
                    binding.expandableLayout0.expand()
                }
            }
        })
        binding.toolbar.setClickOnLeftImage {
            popBack()
        }
        binding.tvSortBy.setOnClickListener {
            bottomSheet.show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.handleAction(CartIntent.LoadItem())
    }

    private var job : Job? = null
    override fun render(state: CartState) {
        binding.progressBar.isVisible = state.isLoading
        job?.cancel()
        job = lifecycleScope.launchWhenResumed {
            bottomSheet.setList(state.itemsFilter)
            state.items.collectLatest {
                binding.tvListSize.text = "${it.size}"
                if (it.isEmpty()) {
                    binding.listEmpty.isVisible = true
                }
                cartAdapter.submitList(it)
            }
        }
    }

    override fun onDestroy() {
        binding.rvListCart.clearOnScrollListeners()
        super.onDestroy()
    }

    override fun getViewBinding(inflater: LayoutInflater): FragmentCartBinding {
        return FragmentCartBinding.inflate(inflater)
    }
}
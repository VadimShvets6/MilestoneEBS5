package com.top1shvetsvadim1.presentation.cart

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.top1shvetsvadim1.coreutils.Action
import com.top1shvetsvadim1.coreutils.BaseAdapter
import com.top1shvetsvadim1.coreutils.BaseFragment
import com.top1shvetsvadim1.presentation.databinding.FragmentCartBinding
import com.top1shvetsvadim1.presentation.delegate.ProductCartDelegate
import com.top1shvetsvadim1.presentation.mvi.CartEvent
import com.top1shvetsvadim1.presentation.mvi.CartIntent
import com.top1shvetsvadim1.presentation.mvi.CartState
import dagger.hilt.android.AndroidEntryPoint
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


    override fun setupViews() {
        binding.progressBar.isVisible = false
        binding.rvListCart.adapter = cartAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.handleAction(CartIntent.LoadItem)
    }

    override fun render(state: CartState) {
        binding.progressBar.isVisible = state.isLoading
        lifecycleScope.launchWhenResumed {
            state.items.collectLatest {
                binding.tvListSize.text = "${it.size}"
                if (it.isEmpty()) {
                    binding.listEmpty.isVisible = true
                }
                cartAdapter.submitList(it)
            }
        }
        binding.rvListCart.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 2) {
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
    }

    override fun getViewBinding(inflater: LayoutInflater): FragmentCartBinding {
        return FragmentCartBinding.inflate(inflater)
    }
}
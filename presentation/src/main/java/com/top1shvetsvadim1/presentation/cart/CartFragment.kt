package com.top1shvetsvadim1.presentation.cart

import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.top1shvetsvadim1.coreutils.BaseFragment
import com.top1shvetsvadim1.presentation.databinding.FragmentCartBinding
import com.top1shvetsvadim1.presentation.mvi.CartEvent
import com.top1shvetsvadim1.presentation.mvi.CartState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : BaseFragment<CartState, CartEvent, CartViewModel, FragmentCartBinding>() {

    override val viewModel: CartViewModel by viewModels()

    override fun setupViews() {
        binding.progressBar.isVisible = false
    }

    override fun render(state: CartState) {
        binding.progressBar.isVisible = state.isLoading
        binding.toolbar.setClickOnLeftImage {
            findNavController().popBackStack()
        }
    }

    override fun getViewBinding(inflater: LayoutInflater): FragmentCartBinding {
        return FragmentCartBinding.inflate(inflater)
    }
}
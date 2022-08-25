package com.top1shvetsvadim1.presentation.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.top1shvetsvadim1.coreui.Colors
import com.top1shvetsvadim1.coreutils.BaseFragment
import com.top1shvetsvadim1.presentation.R
import com.top1shvetsvadim1.presentation.databinding.FragmentDetailBinding
import com.top1shvetsvadim1.presentation.mvi.DetailEvent
import com.top1shvetsvadim1.presentation.mvi.DetailIntent
import com.top1shvetsvadim1.presentation.mvi.DetailState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment :
    BaseFragment<DetailState, DetailEvent, DetailViewModel, FragmentDetailBinding>() {

    override val viewModel: DetailViewModel by viewModels()

    private val args by navArgs<DetailFragmentArgs>()

    override fun setupViews() {
        binding.progressBar.isVisible = true
        setupOnClickListeners()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.handleAction(DetailIntent.LoadItem(args.id))
    }


    override fun handleEffect(effect: DetailEvent) {
        Log.d("OnError", "handelEffect: $effect")
        when (effect) {
            DetailEvent.GeneralException -> {
                with(binding){
                    linear2.isVisible = false
                    tvLabelInformation.isVisible = false
                    tvSize.text = getString(R.string.hint_general_error)
                }
            }
            DetailEvent.ShowNoInternet -> {
                with(binding){
                    linear2.isVisible = false
                    tvLabelInformation.isVisible = false
                    tvSize.text = getString(R.string.hint_check_internet)
                }
                Snackbar.make(binding.root, "No internet connection", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(ContextCompat.getColor(requireActivity(), Colors.color_main_07195C))
                    .setTextColor(ContextCompat.getColor(requireActivity(), Colors.white))
                    .show()
            }
        }
    }

    override fun render(state: DetailState) {
        binding.progressBar.isVisible = state.isLoading
        state.item?.let { product ->
            with(binding) {
                setupOnClickListeners()
                toolbar.setClickOnRightImage {
                    viewModel.handleAction(DetailIntent.ChangeStateItem(product.id))
                    toolbar.setActivatedRightImage(!toolbar.isRightImageActivated())
                }
                toolbar.setActivatedRightImage(product.isFavorite)
                tvProductName.text = product.name
                tvSize.text = product.size
                tvPrice.text = "${product.price}"
                tvSmallPrice.text = "${product.price}"
                tvDescription.text = product.details
                ivLogo.load(product.mainImage)
            }
        }
    }

    private fun setupOnClickListeners() {
        binding.toolbar.setClickOnLeftImage {
            findNavController().popBackStack()
        }
    }

    override fun getViewBinding(inflater: LayoutInflater): FragmentDetailBinding {
        return FragmentDetailBinding.inflate(inflater)
    }
}
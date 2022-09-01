package com.top1shvetsvadim1.presentation.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.top1shvetsvadim1.coreui.BaseAdapter
import com.top1shvetsvadim1.coreui.Colors
import com.top1shvetsvadim1.coreutils.BaseFragment
import com.top1shvetsvadim1.presentation.R
import com.top1shvetsvadim1.presentation.databinding.FragmentDetailBinding
import com.top1shvetsvadim1.presentation.delegate.DescriptionDelegate
import com.top1shvetsvadim1.presentation.delegate.DetailProductDelegate
import com.top1shvetsvadim1.presentation.delegate.ImageDelegate
import com.top1shvetsvadim1.presentation.mvi.DetailEvent
import com.top1shvetsvadim1.presentation.mvi.DetailIntent
import com.top1shvetsvadim1.presentation.mvi.DetailState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BaseFragment<DetailState, DetailEvent, DetailViewModel, FragmentDetailBinding>() {

    override val viewModel: DetailViewModel by viewModels()

    private val args by navArgs<DetailFragmentArgs>()

    //TODO: consider reading about laziness to understand better its concepts.
    private val detailAdapter by BaseAdapter.Builder()
        .setDelegates(ImageDelegate(), DetailProductDelegate(), DescriptionDelegate())
        .buildIn()

    override fun setupViews() {
        binding.progressBar.isVisible = true
        setupOnClickListeners()
        binding.rvDetail.adapter = detailAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.handleAction(DetailIntent.LoadItem(args.id))
    }


    //TODO: Move error handling in base fragment
    override fun handleEffect(effect: DetailEvent) {
        Log.d("OnError", "handelEffect: $effect")
        when (effect) {
            DetailEvent.GeneralException -> {
                with(binding) {
                    linear2.isVisible = false
                    error.isVisible = true
                    error.text = getString(R.string.hint_general_error)
                }
            }
            DetailEvent.ShowNoInternet -> {
                with(binding) {
                    linear2.isVisible = false
                    error.isVisible = true
                    error.text = getString(R.string.hint_check_internet)
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
        Log.d("Test", "${state.isFavorite}")
        binding.toolbar.setActivatedRightImage(state.isFavorite)
        //TODO: use launch IO instead of launchWhenResumed
        lifecycleScope.launchWhenResumed {
            detailAdapter.submitList(state.item)
        }
        with(binding) {
            setupOnClickListeners()
            toolbar.setClickOnRightImage {
                viewModel.handleAction(DetailIntent.ChangeStateItem(args.id))
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
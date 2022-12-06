package com.top1shvetsvadim1.feature_main.detail

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.top1shvetsvadim1.coreui.Colors
import com.top1shvetsvadim1.coreui.Strings
import com.top1shvetsvadim1.coreutils.BaseAdapter
import com.top1shvetsvadim1.coreutils.BaseFragment
import com.top1shvetsvadim1.domain.models.Parameters
import com.top1shvetsvadim1.feature_main.databinding.FragmentDetailBinding
import com.top1shvetsvadim1.feature_main.delegate.DetailProductDelegate
import com.top1shvetsvadim1.feature_main.delegate.ImageDelegate
import com.top1shvetsvadim1.feature_main.delegate.ItemTextsDelegate
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailFragment : BaseFragment<DetailState, DetailEvent, DetailViewModel, FragmentDetailBinding>() {

    override val viewModel: DetailViewModel by viewModels()

    private val args by navArgs<DetailFragmentArgs>()

    private val detailAdapter by BaseAdapter.Builder()
        .setDelegates(ItemTextsDelegate(), ImageDelegate(), DetailProductDelegate())
        .buildIn()

    override fun setupViews() {
        binding.progressBar.isVisible = true
        setupOnClickListeners()
        binding.toolbar.setClickOnRightImage {
            viewModel.handleAction(DetailIntent.ChangeStateItem(args.id))
        }
        binding.rvDetail.adapter = detailAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val transition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move).apply {
                duration = 100
            }
        sharedElementEnterTransition = transition
        sharedElementReturnTransition = transition
        viewModel.handleAction(DetailIntent.LoadItem(args.id))
        viewModel.handleAction(DetailIntent.Remote(Parameters.Text, "remote_config_test_text"))
        viewModel.handleAction(DetailIntent.Remote(Parameters.Color, "button_color_test"))
    }


    //TODO: old todo
    //TODO: Move error handling in base fragment
    override fun handleEffect(effect: DetailEvent) {
        when (effect) {
            DetailEvent.GeneralException -> {
                with(binding) {
                    linear2.isVisible = false
                    error.isVisible = true
                    error.text = getString(Strings.hint_general_error)
                }
            }
            DetailEvent.ShowNoInternet -> {
                with(binding) {
                    linear2.isVisible = false
                    error.isVisible = true
                    error.text = getString(Strings.hint_check_internet)
                }
                createSnackBar("No internet connection", Colors.color_main_07195C, Colors.white)
            }
        }
    }

    override fun render(state: DetailState) {
        binding.progressBar.isVisible = state.isLoading
        //TODO: one time actions should be in separate function called once in onViewCreated
        binding.toolbar.setActivatedRightImage(state.isFavorite)
        detailAdapter.submitList(state.item)

        binding.buyNow.text = state.text
    }

    private fun setupOnClickListeners() {
        binding.toolbar.setClickOnLeftImage { popBack() }
        binding.addToCart.setOnClickListener {
            viewModel.handleAction(DetailIntent.AddItemToCart(args.id))
            createSnackBar("Added to cart", Colors.color_main_07195C, Colors.white)
        }
        binding.buyNow.setOnClickListener {
            viewModel.handleAction(DetailIntent.AddItemToCart(args.id))
            navigateTo(DetailFragmentDirections.actionDetailFragmentToCartFragment())
        }
    }

    override fun getViewBinding(inflater: LayoutInflater): FragmentDetailBinding {
        return FragmentDetailBinding.inflate(inflater)
    }
}
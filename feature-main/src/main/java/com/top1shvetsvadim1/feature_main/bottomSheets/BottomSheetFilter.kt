package com.top1shvetsvadim1.feature_main.bottomSheets

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.StyleRes
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.top1shvetsvadim1.coreui.Drawable
import com.top1shvetsvadim1.coreutils.Action
import com.top1shvetsvadim1.coreutils.BaseAdapter
import com.top1shvetsvadim1.coreutils.BaseUIModel
import com.top1shvetsvadim1.domain.models.Filters
import com.top1shvetsvadim1.feature_main.databinding.BootomSheetBinding
import com.top1shvetsvadim1.feature_main.delegate.ItemTextsDelegate

class BottomSheetFilter(
    context: Context,
    @StyleRes style: Int = com.top1shvetsvadim1.coreui.R.style.BottomSheetDialogTheme,
    val onFilterPressed: (Filters) -> Unit
) : BottomSheetDialog(context, style) {

    private val filterAdapter by BaseAdapter.Builder()
        .setDelegates(ItemTextsDelegate())
        .setActionProcessor(::onClickFilter)
        .buildIn()

    private fun onClickFilter(action: Action) {
        when (action) {
            is ItemTextsDelegate.ItemActionClick.OnItemClicked -> {
                when (action.id) {
                    1 -> {
                        onFilterPressed(Filters.PRICE)
                        dismiss()
                    }
                    2 -> {
                        onFilterPressed(Filters.SIZE)
                        dismiss()
                    }
                    3 -> {
                        onFilterPressed(Filters.COLOR)
                        dismiss()
                    }
                    4 -> {
                        onFilterPressed(Filters.RESET)
                        dismiss()
                    }
                }
            }
        }
    }


    private val binding by lazy {
        BootomSheetBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val dividerItemDecoration = DividerItemDecoration(context, RecyclerView.VERTICAL)
        getDrawable(context, Drawable.divider_drawable)?.let { dividerItemDecoration.setDrawable(it) }
        binding.filterRv.addItemDecoration(dividerItemDecoration)
        binding.filterRv.adapter = filterAdapter
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    fun setList(list: List<BaseUIModel>) {
        filterAdapter.submitList(list)
    }
}

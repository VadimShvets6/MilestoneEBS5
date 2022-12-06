package com.top1shvetsvadim1.coreutils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter

class BasePagingAdapter private constructor(private val delegates: List<ItemDelegate<BaseUIModel, BaseViewHolder<BaseUIModel>>>) :
    PagingDataAdapter<BaseUIModel, BaseViewHolder<BaseUIModel>>(DefaultDiffUtil(delegates)) {

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position) ?: Unit
        return delegates.find { it.clazz == item::class}?.let { delegates.indexOf(it) }
            ?: throw Exception("Unregistered viewHolder")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<BaseUIModel> {
        return delegates[viewType].getOrCreateViewHolder(
            LayoutInflater.from(parent.context),
            parent
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder<BaseUIModel>, position: Int) {
        onBindViewHolder(holder, position, mutableListOf())
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<BaseUIModel>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val item = getItem(position)
        if (item != null) {
            delegates[getItemViewType(position)].onBindViewHolder(holder, item, payloads)
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Builder {
        private val delegates: MutableList<ItemDelegate<BaseUIModel, BaseViewHolder<BaseUIModel>>> = mutableListOf()

        fun setDelegates(vararg delegate: ItemDelegate<out BaseUIModel, out BaseViewHolder<out BaseUIModel>>): Builder {
            delegate.forEach {
                delegates.add(it as ItemDelegate<BaseUIModel, BaseViewHolder<BaseUIModel>>)
            }
            return this
        }

        fun setActionProcessor(processor: (Action) -> Unit): Builder {
            delegates.forEach {
                it.action = processor
            }
            return this
        }

        fun buildIn() = lazy(LazyThreadSafetyMode.SYNCHRONIZED) { BasePagingAdapter(delegates) }
    }
}
package com.top1shvetsvadim1.coreui

import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import androidx.appcompat.resources.Compatibility.Api21Impl.inflate
import androidx.core.content.res.ColorStateListInflaterCompat.inflate
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import com.top1shvetsvadim1.coreutils.BaseUIModel
import com.top1shvetsvadim1.coreutils.BaseViewHolder
import com.top1shvetsvadim1.coreutils.DefaultDiffUtil
import kotlin.reflect.KClass

class BaseAdapter private constructor(private val delegates: List<ItemDelegate<BaseUIModel, BaseViewHolder<BaseUIModel>>>) :
    ListAdapter<BaseUIModel, BaseViewHolder<BaseUIModel>>(DefaultDiffUtil()) {

    override fun getItemViewType(position: Int): Int {
        return delegates.find { it.clazz == getItem(position)::class }
            ?.let { delegates.indexOf(it) }
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
        delegates[getItemViewType(position)].onBindViewHolder(holder, item, payloads)
    }

    @Suppress("UNCHECKED_CAST")
    class Builder {
        private val delegates: MutableList<ItemDelegate<BaseUIModel, BaseViewHolder<BaseUIModel>>> =
            mutableListOf()

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

        fun buildIn() = lazy(LazyThreadSafetyMode.SYNCHRONIZED) { BaseAdapter(delegates) }
    }
}

abstract class ItemDelegate<T : BaseUIModel, H : BaseViewHolder<T>>(
    val clazz: KClass<T>
) {
    fun getOrCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): H {
        return createViewHolder(inflater, parent)
    }

    fun pushAction(action: Action) {
        this.action(action)
    }

    abstract fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup): H

    open fun onBindViewHolder(holder: H, item: T, payload: MutableList<Any>) {
        holder.bind(item)
        holder.setOnClickListeners(item)
    }

    var action: (Action) -> Unit = { }
}

interface Action
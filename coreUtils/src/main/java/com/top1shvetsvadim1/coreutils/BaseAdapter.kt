package com.top1shvetsvadim1.coreutils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import kotlin.reflect.KClass

//TODO: move to Core UI
class BaseAdapter private constructor(private val delegates: List<ItemDelegate<BaseUIModel, BaseViewHolder<BaseUIModel>>>) :
    ListAdapter<BaseUIModel, BaseViewHolder<BaseUIModel>>(DefaultDiffUtil(delegates)) {

    override fun getItemViewType(position: Int): Int {
        return delegates.find { it.clazz == getItem(position)::class }
            ?.let { delegates.indexOf(it) }
            ?: throw Exception("Unregistered viewHolder")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<BaseUIModel> {
        //TODO: hope you've understood what you done, 'cause I see copied functions from Delegate Adapter
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
        holder.setOnClickListeners(item)
    }

    @Suppress("UNCHECKED_CAST")
    class Builder {
        private val delegates: MutableList<ItemDelegate<BaseUIModel, BaseViewHolder<BaseUIModel>>> =
            mutableListOf()

        //TODO: hope you've understood what you done, 'cause I see copied functions from Delegate Adapter
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

        //TODO: hope you've understood what you done, 'cause I see copied functions from Delegate Adapter
        fun buildIn() = lazy(LazyThreadSafetyMode.SYNCHRONIZED) { BaseAdapter(delegates) }
    }
}

//TODO: hope you've understood what you done, 'cause I see copied functions from Delegate Adapter
abstract class ItemDelegate<T : BaseUIModel, H : BaseViewHolder<T>>(
    val clazz: KClass<T>,
    //TODO: you do not need layout ID here, use binding
    private val layoutID: Int
) {
    fun getOrCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): H {
        val view = inflater.inflate(layoutID, parent, false)
        return createViewHolder(view)
    }

    fun pushAction(action: Action) {
        this.action(action)
    }

    abstract fun createViewHolder(view: View): H

    open fun onBindViewHolder(holder: H, item: T, payload: MutableList<Any>) {
        holder.bind(item)
        holder.setOnClickListeners(item)
    }

    var action: (Action) -> Unit = { }
}

interface Action
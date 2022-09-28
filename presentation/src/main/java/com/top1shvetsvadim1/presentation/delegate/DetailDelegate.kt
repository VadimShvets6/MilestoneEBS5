package com.top1shvetsvadim1.presentation.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import com.flexeiprata.novalles.annotations.AutoBindViewHolder
import com.flexeiprata.novalles.annotations.Instruction
import com.flexeiprata.novalles.annotations.PrimaryTag
import com.flexeiprata.novalles.annotations.UIModel
import com.flexeiprata.novalles.interfaces.Instructor
import com.flexeiprata.novalles.interfaces.Novalles
import com.top1shvetsvadim1.coreutils.BaseUIModel
import com.top1shvetsvadim1.coreutils.BaseViewHolder
import com.top1shvetsvadim1.coreutils.ItemDelegate
import com.top1shvetsvadim1.presentation.databinding.DetailProductItemBinding
import java.util.*

@UIModel
data class DetailProductUIModel(
    @PrimaryTag val id: Int,
    val name: String,
    val size: String,
    val price: Int,
) : BaseUIModel()

@Instruction(DetailProductUIModel::class)
@AutoBindViewHolder(DetailProductDelegate.DetailProductViewHolder::class)
class DetailInstructor : Instructor

class DetailProductDelegate :
    ItemDelegate<DetailProductUIModel, DetailProductDelegate.DetailProductViewHolder>(
        DetailProductUIModel::class
    ) {
    private val inspector = Novalles.provideInspectorFromUiModel(DetailProductUIModel::class)

    override fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): DetailProductDelegate.DetailProductViewHolder {
        return DetailProductViewHolder(DetailProductItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(
        holder: DetailProductViewHolder,
        item: DetailProductUIModel,
        payload: MutableList<Any>
    ) {
        val instructor = DetailInstructor()
        inspector.inspectPayloads(payload, instructor, viewHolder = holder) {
            holder.bind(item)
        }
    }

    inner class DetailProductViewHolder(private val binding: DetailProductItemBinding) :
        BaseViewHolder<DetailProductUIModel>(binding) {
        override fun bind(item: DetailProductUIModel) {
            setName(item.name)
            setPrice(item.price)
            setSize(item.size)
        }

        fun setName(name: String) {
            binding.tvProductName.text = name
        }

        fun setSize(size: String) {
            binding.tvSize.text = size
        }

        fun setPrice(price: Int) {
            binding.tvPrice.text = String.format(Locale.getDefault(), "$%d,-", price)
            binding.tvSmallPrice.text = String.format(Locale.getDefault(), "$%d,-", price)
        }
    }
}


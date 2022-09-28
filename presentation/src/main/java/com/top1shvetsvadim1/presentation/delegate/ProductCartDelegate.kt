package com.top1shvetsvadim1.presentation.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import coil.load
import com.flexeiprata.novalles.annotations.*
import com.flexeiprata.novalles.interfaces.Instructor
import com.flexeiprata.novalles.interfaces.Novalles
import com.squareup.moshi.JsonClass
import com.top1shvetsvadim1.coreutils.Action
import com.top1shvetsvadim1.coreutils.BaseUIModel
import com.top1shvetsvadim1.coreutils.BaseViewHolder
import com.top1shvetsvadim1.coreutils.ItemDelegate
import com.top1shvetsvadim1.presentation.databinding.ProductCartItemBinding
import java.util.*

//TODO: better to place primary tag as first value
@JsonClass(generateAdapter = true)
@UIModel
data class ProductCartUIModel(
    val name: String,
    val sizes: String,
    val price: Int,
    val mainImage: String,
    @PrimaryTag val id: Int,
    val inCart: Boolean,
    val count: Int
) : BaseUIModel()

@Instruction(ProductCartUIModel::class)
@AutoBindViewHolder(ProductCartDelegate.CartViewHolder::class)
class ProductCartsInstructor : Instructor

class ProductCartDelegate : ItemDelegate<ProductCartUIModel, ProductCartDelegate.CartViewHolder>(
    ProductCartUIModel::class
) {

    private val inspector = Novalles.provideInspectorFromUiModel(ProductCartUIModel::class)

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup): CartViewHolder {
        return CartViewHolder(ProductCartItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: CartViewHolder, item: ProductCartUIModel, payload: MutableList<Any>) {
        val instructor = ProductCartsInstructor()
        inspector.inspectPayloads(payload, instructor, viewHolder = holder) {
            holder.bind(item)
        }
        holder.setOnClickListeners(item)
    }

    inner class CartViewHolder(private val binding: ProductCartItemBinding) :
        BaseViewHolder<ProductCartUIModel>(binding) {
        override fun bind(item: ProductCartUIModel) {
            setMainImage(item.mainImage)
            setPrice(item.price)
            setSizes(item.sizes)
            setInCart(item.inCart)
            setName(item.name)
            setItemCount(item.count)
        }

        override fun setOnClickListeners(item: ProductCartUIModel) {
            binding.buttonDelete.setOnClickListener {
                pushAction(ActionProductCartAdapter.OnProductDeleteClicked(item.id, !item.inCart))
            }
        }

        fun setItemCount(count: Int) {
            binding.itemCount.text = "$count"
        }

        fun setName(name: String) {
            binding.tvProductName.text = name
        }

        fun setInCart(cart: Boolean) {

        }

        fun setSizes(size: String) {
            binding.tvSize.text = size
        }

        fun setPrice(price: Int) {
            binding.tvPrice.text = String.format(Locale.getDefault(), "$%d,-", price)
            binding.tvSmallPrice.text = String.format(Locale.getDefault(), "$%d,-", price)
        }

        fun setMainImage(image: String) {
            binding.ivProduct.load(image)
        }
    }

    sealed interface ActionProductCartAdapter : Action {
        data class OnProductDeleteClicked(val id: Int, val setFavorite: Boolean) : ActionProductCartAdapter
    }
}
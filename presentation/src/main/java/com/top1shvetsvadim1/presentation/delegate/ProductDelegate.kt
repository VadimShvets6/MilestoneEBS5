package com.top1shvetsvadim1.presentation.delegate

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import coil.load
import com.flexeiprata.novalles.annotations.AutoBindViewHolder
import com.flexeiprata.novalles.annotations.Instruction
import com.flexeiprata.novalles.annotations.PrimaryTag
import com.flexeiprata.novalles.annotations.UIModel
import com.flexeiprata.novalles.interfaces.Instructor
import com.flexeiprata.novalles.interfaces.Novalles
import com.squareup.moshi.JsonClass
import com.top1shvetsvadim1.coreui.Anim
import com.top1shvetsvadim1.coreutils.Action
import com.top1shvetsvadim1.coreutils.BaseUIModel
import com.top1shvetsvadim1.coreutils.BaseViewHolder
import com.top1shvetsvadim1.coreutils.ItemDelegate
import com.top1shvetsvadim1.presentation.databinding.ProductItemBinding

@JsonClass(generateAdapter = true)
@UIModel
data class ProductUIModel(
    val name: String,
    val sizes: String,
    val price: Int,
    val mainImage: String,
    @PrimaryTag val id: Int,
    val isFavorite: Boolean,
    val inCart: Boolean
) : BaseUIModel()

@Instruction(ProductUIModel::class)
@AutoBindViewHolder(ProductDelegate.ProductViewHolder::class)
class ProductInstructor : Instructor

class ProductDelegate : ItemDelegate<ProductUIModel, ProductDelegate.ProductViewHolder>(
    ProductUIModel::class
) {
    private val inspector = Novalles.provideInspectorFromUiModel(ProductUIModel::class)

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup): ProductViewHolder {
        return ProductViewHolder(ProductItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(
        holder: ProductViewHolder,
        item: ProductUIModel,
        payload: MutableList<Any>
    ) {
        Log.d("Test", "OnBind: $payload")
        val instructor = ProductInstructor()
        inspector.inspectPayloads(payload, instructor, viewHolder = holder) {
            holder.bind(item)
        }
        holder.setOnClickListeners(item)
    }

    inner class ProductViewHolder(private val binding: ProductItemBinding) :
        BaseViewHolder<ProductUIModel>(binding) {
        override fun bind(item: ProductUIModel) {
            setName(item.name)
            setPrice(item.price)
            setMainImage(item.mainImage)
            setIsFavorite(item.isFavorite)
            setInCart(item.inCart)
            setSizes(item.sizes)
            setOnClickListeners(item)
            // setAnim()
        }

        private fun setAnim() {
            binding.cardView.startAnimation(
                AnimationUtils.loadAnimation(
                    binding.cardView.context,
                    Anim.anim_recycler
                )
            )
        }

        override fun setOnClickListeners(item: ProductUIModel) {
            binding.buttonHeart.setOnClickListener {
                pushAction(ActionProductAdapter.OnProductFavoriteClicked(item.id, !item.isFavorite))
            }
            binding.buttonBuschet.setOnClickListener {
                pushAction(ActionProductAdapter.OnProductCartClicked(item.id, !item.inCart))
            }
            binding.root.setOnClickListener {
                pushAction(ActionProductAdapter.OnProductClicked(item))
            }
        }

        fun setName(name: String) {
            binding.tvProductName.text = name
        }

        fun setPrice(price: Int) {
            binding.tvPrice.text = "$price"
            binding.tvSmallPrice.text = "$price"
        }

        fun setMainImage(picture: String) {
            binding.ivProduct.load(picture)
        }

        fun setIsFavorite(favorite: Boolean) {
            binding.buttonHeart.isActivated = favorite
        }

        fun setInCart(cart: Boolean) {
            binding.buttonBuschet.isActivated = cart
        }

        fun setSizes(size: String) {
            binding.tvSize.text = size
        }
    }

    sealed interface ActionProductAdapter : Action {
        data class OnProductFavoriteClicked(val id: Int, val setFavorite: Boolean) : ActionProductAdapter
        data class OnProductClicked(val productItem: ProductUIModel) : ActionProductAdapter
        data class OnProductCartClicked(val id: Int, val setCart: Boolean) : ActionProductAdapter
    }
}
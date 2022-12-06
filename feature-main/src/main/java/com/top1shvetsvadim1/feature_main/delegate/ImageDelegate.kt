package com.top1shvetsvadim1.feature_main.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import coil.load
import com.flexeiprata.novalles.annotations.AutoBindViewHolder
import com.flexeiprata.novalles.annotations.Instruction
import com.flexeiprata.novalles.annotations.PrimaryTag
import com.flexeiprata.novalles.annotations.UIModel
import com.flexeiprata.novalles.interfaces.Instructor
import com.flexeiprata.novalles.interfaces.Novalles
import com.top1shvetsvadim1.coreutils.BaseUIModel
import com.top1shvetsvadim1.coreutils.BaseViewHolder
import com.top1shvetsvadim1.coreutils.ItemDelegate
import com.top1shvetsvadim1.feature_main.databinding.ImageDetailItemBinding

@UIModel
data class ImageUIModel(
    @PrimaryTag val id: Int,
    val image: String
) : BaseUIModel()

@Instruction(ImageUIModel::class)
@AutoBindViewHolder(ImageDelegate.ImageDetailViewHolder::class)
class ImageInstructor : Instructor

class ImageDelegate :
    ItemDelegate<ImageUIModel, ImageDelegate.ImageDetailViewHolder>(
        ImageUIModel::class
    ) {

    private val inspector = Novalles.provideInspectorFromUiModel(ImageUIModel::class)

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup): ImageDetailViewHolder {
        return ImageDetailViewHolder(ImageDetailItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(
        holder: ImageDetailViewHolder,
        item: ImageUIModel,
        payload: MutableList<Any>
    ) {
        val instructor = ImageInstructor()
        inspector.inspectPayloads(payload, instructor, viewHolder = holder) {
            holder.bind(item)
        }
    }

    inner class ImageDetailViewHolder(
        private val binding: ImageDetailItemBinding
    ) : BaseViewHolder<ImageUIModel>(binding) {

        override fun bind(item: ImageUIModel) {
            setImage(item.image)
        }

        fun setImage(image: String) {
            binding.ivLogo.load(image)
        }
    }
}
package com.top1shvetsvadim1.feature_main.customView

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import coil.load
import com.top1shvetsvadim1.feature_main.databinding.DialogSaleBinding

class CustomDialogSale(
    context: Context,
    private val title: String,
    private val message: String,
    private val id: Int,
    private val icon: String,
    val onClick: (Int) -> Unit
) : Dialog(context) {

    private val binding = DialogSaleBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun createDialog() {
        binding.title.text = title
        binding.message.text = message
        binding.icon.load(icon)
        setCancelable(false)
        show()
        binding.readBtn.setOnClickListener {
            onClick(id)
            dismiss()
        }
    }
}
package com.top1shvetsvadim1.presentation.customView

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import coil.load
import com.top1shvetsvadim1.presentation.databinding.DialogSaleBinding

class CustomDialogSale(
    context: Context
) : Dialog(context) {

    private val binding by lazy {
        DialogSaleBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun createDialog(title: String, message: String, id: Int, icon: String, onClick: (Int) -> Unit) {
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

    override fun onBackPressed() {
        dismiss()
    }
}
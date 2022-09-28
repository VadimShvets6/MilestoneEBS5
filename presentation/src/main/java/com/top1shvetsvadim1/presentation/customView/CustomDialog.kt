package com.top1shvetsvadim1.presentation.customView

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.top1shvetsvadim1.presentation.databinding.CustomDialogBinding

class CustomDialog(context: Context) : Dialog(context) {

    private val binding by lazy {
        CustomDialogBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    fun showDialog() {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        show()
        binding.readBtn.setOnClickListener {
            dismiss()
        }
    }

    override fun onBackPressed() {
        dismiss()
    }
}
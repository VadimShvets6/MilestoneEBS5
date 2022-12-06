package com.top1shvetsvadim1.feature_main

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.top1shvetsvadim1.coreui.FeatureMainContract

class FeatureMainContractImpl : FeatureMainContract {
    override fun show(navController: NavController) {
        navController.navigate(R.id.main_navigation)
    }
}
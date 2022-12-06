package com.top1shvetsvadim1.feature_auth

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.top1shvetsvadim1.coreui.FeatureAuthContract

class FeatureAuthContractImpl : FeatureAuthContract{

    override fun show( navController: NavController) {
        navController.navigate(R.id.auth_nav)
    }
}
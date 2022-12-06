package com.top1shvetsvadim1.feature_main

import com.top1shvetsvadim1.coreui.FeatureAuthContract
import com.top1shvetsvadim1.coreui.FeatureMainContract
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FeatureMainModule {

    @Singleton
    @Provides
    fun providesFeatureARouteContract(): FeatureMainContract = FeatureMainContractImpl()
}
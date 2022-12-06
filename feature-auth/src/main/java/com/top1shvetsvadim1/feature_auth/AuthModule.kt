package com.top1shvetsvadim1.feature_auth

import com.top1shvetsvadim1.coreui.FeatureAuthContract
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.internal.managers.ApplicationComponentManager
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AuthModule {

    @Singleton
    @Provides
    fun providesFeatureARouteContract(): FeatureAuthContract = FeatureAuthContractImpl()
}
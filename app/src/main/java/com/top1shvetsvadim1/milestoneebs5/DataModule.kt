package com.top1shvetsvadim1.milestoneebs5

import com.top1shvetsvadim1.data.ProductRepositoryImpl
import com.top1shvetsvadim1.data.SettingRepositoryImpl
import com.top1shvetsvadim1.domain.repository.ProductRepository
import com.top1shvetsvadim1.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

//TODO: put in in proper package
@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindRepository(impl: ProductRepositoryImpl): ProductRepository

    @Binds
    abstract fun bindSettingRepository(impl: SettingRepositoryImpl): SettingsRepository
}
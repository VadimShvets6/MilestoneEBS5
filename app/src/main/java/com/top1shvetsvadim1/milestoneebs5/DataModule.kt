package com.top1shvetsvadim1.milestoneebs5

import com.top1shvetsvadim1.data.ProductRepositoryImpl
import com.top1shvetsvadim1.domain.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindRepository(impl: ProductRepositoryImpl): ProductRepository
}
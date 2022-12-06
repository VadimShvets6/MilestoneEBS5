package com.top1shvetsvadim1.data

import com.top1shvetsvadim1.data.impl.*
import com.top1shvetsvadim1.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindRepository(impl: ProductRepositoryImpl): ProductRepository

    @Binds
    abstract fun bindSettingRepository(impl: SettingRepositoryImpl): SettingsRepository

    @Binds
    abstract fun bindFavoriteRepository(impl: FavoriteRepositoryImpl): FavoriteRepository

    @Binds
    abstract fun bindCartRepository(impl: CartRepositoryImpl): CartRepository

    @Binds
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}
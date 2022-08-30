package com.top1shvetsvadim1.data

import android.content.Context
import androidx.room.Room
import com.top1shvetsvadim1.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModel {
    //TODO: bad db name, it can be not only for favourites
    private const val DB_NAME = "favorite_database.db"

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        DB_NAME
    ).fallbackToDestructiveMigration().build() //TODO: hope you understand what is fallbackToDestructiveMigration

    @Singleton
    @Provides
    fun provideFavoriteDao(db: AppDatabase) = db.favoriteItemDao()
}
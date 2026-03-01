package com.elytrastudios.assessment.di

import android.content.Context
import androidx.room.Room
import com.elytrastudios.assessment.data.local.AppDatabase
import com.elytrastudios.assessment.data.local.DogDao
import com.elytrastudios.assessment.data.local.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "elytra_db"
        ).build()

    @Provides
    fun provideDogDao(db: AppDatabase): DogDao = db.dogDao()

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

}

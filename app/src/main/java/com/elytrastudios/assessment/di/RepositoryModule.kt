package com.elytrastudios.assessment.di

import com.elytrastudios.assessment.domain.repository.DogRepository
import com.elytrastudios.assessment.domain.repository.UserRepository
import com.elytrastudios.assessment.data.repository.DogRepositoryImpl
import com.elytrastudios.assessment.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDogRepository(
        impl: DogRepositoryImpl
    ): DogRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository


}

package com.elytrastudios.assessment.di

import com.elytrastudios.assessment.data.remote.DogApi
import com.elytrastudios.assessment.data.remote.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.elytrastudios.assessment.BuildConfig
import com.elytrastudios.assessment.util.AppLogger
import com.elytrastudios.assessment.util.LoggingController

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            if (LoggingController.enabled.value) {
                AppLogger.d("OkHttp", message)
            }
        }.apply {
            level = when (BuildConfig.FLAVOR) {
                "prod" -> HttpLoggingInterceptor.Level.NONE
                else -> HttpLoggingInterceptor.Level.BODY
            }
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }


    @Provides
    @Singleton
    @UserRetrofit
    fun provideUserRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.USER_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    @Provides
    @Singleton
    @DogRetrofit
    fun provideDogRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.DOG_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    @Provides
    @Singleton
    fun provideUserApi(@UserRetrofit userRetrofit: Retrofit): UserApi =
        userRetrofit.create(UserApi::class.java)

    @Provides
    @Singleton
    fun provideDogApi(@DogRetrofit dogRetrofit: Retrofit): DogApi =
        dogRetrofit.create(DogApi::class.java)
}
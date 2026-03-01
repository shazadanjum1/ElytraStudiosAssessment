package com.elytrastudios.assessment.data.remote

import com.elytrastudios.assessment.data.model.UserResponse
import retrofit2.http.GET
import retrofit2.http.Path


interface UserApi {
    @GET("users")
    suspend fun getUsers(): List<UserResponse>

    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: Int): UserResponse
}

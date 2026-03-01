package com.elytrastudios.assessment.data.remote

import com.elytrastudios.assessment.data.model.DogResponse
import retrofit2.http.GET


interface DogApi {
    @GET("breeds/list/all")
    suspend fun getBreeds(): DogResponse
}

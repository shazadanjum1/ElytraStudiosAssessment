package com.elytrastudios.assessment.domain.repository

import com.elytrastudios.assessment.data.model.DogBreed

interface DogRepository {
    suspend fun getDogBreeds(): Result<List<DogBreed>>
}
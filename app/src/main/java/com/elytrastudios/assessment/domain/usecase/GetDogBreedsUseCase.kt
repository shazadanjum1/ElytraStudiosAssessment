package com.elytrastudios.assessment.domain.usecase

import com.elytrastudios.assessment.data.model.DogBreed
import com.elytrastudios.assessment.domain.repository.DogRepository
import javax.inject.Inject

class GetDogBreedsUseCase @Inject constructor(
    private val repository: DogRepository
) {
    suspend operator fun invoke(): Result<List<DogBreed>> = repository.getDogBreeds()
}

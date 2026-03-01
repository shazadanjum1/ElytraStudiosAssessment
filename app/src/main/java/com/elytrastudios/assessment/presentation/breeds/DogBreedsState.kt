package com.elytrastudios.assessment.presentation.breeds

import com.elytrastudios.assessment.data.model.DogBreed


sealed class DogBreedsState {
    object Loading : DogBreedsState()
    data class Success(val breeds: List<DogBreed>) : DogBreedsState()
    data class Error(val message: String) : DogBreedsState()
}


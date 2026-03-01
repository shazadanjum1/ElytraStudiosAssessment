package com.elytrastudios.assessment.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dog_breeds")
data class DogBreedEntity(
    @PrimaryKey val name: String,
    val subBreeds: String
)

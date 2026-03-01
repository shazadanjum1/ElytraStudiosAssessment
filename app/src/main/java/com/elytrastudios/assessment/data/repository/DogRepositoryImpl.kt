package com.elytrastudios.assessment.data.repository

import android.app.Application
import com.elytrastudios.assessment.data.local.DogBreedEntity
import com.elytrastudios.assessment.data.local.DogDao
import com.elytrastudios.assessment.data.mock.MockDataSource
import com.elytrastudios.assessment.data.model.DogBreed
import com.elytrastudios.assessment.data.remote.DogApi
import com.elytrastudios.assessment.domain.repository.DogRepository
import javax.inject.Inject
import com.elytrastudios.assessment.BuildConfig
import com.elytrastudios.assessment.util.AppLogger
import com.elytrastudios.assessment.util.NetworkUtils


class DogRepositoryImpl @Inject constructor(
    private val api: DogApi,
    private val dao: DogDao,
    private val mockDataSource: MockDataSource,
    private val app: Application
) : DogRepository {

    override suspend fun getDogBreeds(): Result<List<DogBreed>> {
        return if (BuildConfig.USE_MOCK) {
            // If mock mode is enabled (dev/mock flavor), return mock data
            AppLogger.d("DogRepository", "Fetching dog breeds from mock data")
            Result.success(mockDataSource.getMockBreeds())
        } else {
            // If not using mock, check network availability first
            if (!NetworkUtils.isNetworkAvailable(app)) {
                // No network → fallback to cached breeds
                return getCachedBreedsOrError()
            }
            try {
                // Fetch breeds from API
                AppLogger.d("DogRepository", "Fetching dog breeds from API")
                val response = api.getBreeds()
                AppLogger.d("DogRepository", "Received ${response.message.size} breeds")

                // Map API response to Room entities
                val entities = response.message.map { (breed, subs) ->
                    DogBreedEntity(breed, subs.joinToString(","))
                }
                // Save entities to local DB for offline caching
                dao.insertAll(entities)

                // Convert entities to domain models and return success
                Result.success(entities.map { it.toDomain() })
            } catch (e: Exception) {
                // On API error, log and fallback to cache
                AppLogger.e("DogRepository", "Error fetching dog breeds", e)
                getCachedBreedsOrError()
            }
        }
    }

    // Helper: returns cached breeds if available, otherwise error
    private suspend fun getCachedBreedsOrError(): Result<List<DogBreed>> {
        val cached = dao.getAll()
        return if (cached.isNotEmpty()) {
            AppLogger.w("DogRepository", "Returning ${cached.size} cached breeds")
            Result.success(cached.map { it.toDomain() })
        } else {
            AppLogger.e("DogRepository", "No cached breeds available")
            Result.failure(Exception("No internet connection and no cached breeds"))
        }
    }

    // Extension function: maps DB entity to domain model
    fun DogBreedEntity.toDomain(): DogBreed =
        DogBreed(name, subBreeds.split(","))
}



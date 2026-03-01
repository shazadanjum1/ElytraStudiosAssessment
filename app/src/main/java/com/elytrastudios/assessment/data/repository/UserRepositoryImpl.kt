package com.elytrastudios.assessment.data.repository

import android.app.Application
import com.elytrastudios.assessment.data.local.UserDao
import com.elytrastudios.assessment.data.local.UserEntity
import com.elytrastudios.assessment.data.mock.MockDataSource
import com.elytrastudios.assessment.data.model.UserResponse
import com.elytrastudios.assessment.data.remote.UserApi
import com.elytrastudios.assessment.domain.repository.UserRepository
import javax.inject.Inject
import com.elytrastudios.assessment.BuildConfig
import com.elytrastudios.assessment.data.model.Address
import com.elytrastudios.assessment.data.model.Company
import com.elytrastudios.assessment.data.model.Geo
import com.elytrastudios.assessment.util.AppLogger
import com.elytrastudios.assessment.util.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class UserRepositoryImpl @Inject constructor(
    private val api: UserApi,
    private val userDao: UserDao,
    private val mockDataSource: MockDataSource,
    private val app: Application
) : UserRepository {

    // Fetch all users
    override suspend fun getUsers(): Result<List<UserResponse>> {
        return if (BuildConfig.USE_MOCK) {
            // If mock mode is enabled (dev/mock flavor), return mock data
            Result.success(mockDataSource.getMockUsers())
        } else {
            // If not using mock, check network availability first
            if (!NetworkUtils.isNetworkAvailable(app)) {
                // No network → fallback to cached users
                return getCachedUsersOrError()
            }
            try {
                // Fetch users from API
                val response = api.getUsers()
                AppLogger.d("UserRepository", "Received ${response.size} users")

                // Save API response to local DB for offline caching
                userDao.insertAll(response.map { it.toEntity() })

                // Return API response directly
                Result.success(response)
            } catch (e: Exception) {
                // On API error, log and fallback to cache
                AppLogger.e("UserRepository", "Error fetching users", e)
                getCachedUsersOrError()
            }
        }
    }

    // Fetch single user by ID
    override suspend fun getUser(id: Int): Result<UserResponse> {
        return if (BuildConfig.USE_MOCK) {
            // If mock mode enabled, search mock data for user
            mockDataSource.getMockUsers().find { it.id == id }
                ?.let { Result.success(it) }
                ?: Result.failure(Exception("User not found in mock data"))
        } else {
            // If not using mock, check network availability first
            if (!NetworkUtils.isNetworkAvailable(app)) {
                // No network → fallback to cached user
                return getCachedUserOrError(id)
            }
            try {
                // Fetch user from API
                val response = api.getUser(id)
                AppLogger.d("UserRepository", "Fetched user $id from API")

                // Save user to local DB for offline caching
                userDao.insert(response.toEntity())

                Result.success(response)
            } catch (e: Exception) {
                // On API error, log and fallback to cache
                AppLogger.e("UserRepository", "Error fetching user $id", e)
                getCachedUserOrError(id)
            }
        }
    }

    // Helper: returns cached users if available, otherwise error
    private suspend fun getCachedUsersOrError(): Result<List<UserResponse>> {
        val cached = userDao.getAll()
        return if (cached.isNotEmpty()) {
            AppLogger.w("UserRepository", "Returning ${cached.size} cached users")
            Result.success(cached.map { it.toResponse() })
        } else {
            AppLogger.e("UserRepository", "No cached users available")
            Result.failure(Exception("No internet connection and no cached users"))
        }
    }

    // Helper: returns cached user by ID if available, otherwise error
    private suspend fun getCachedUserOrError(id: Int): Result<UserResponse> {
        val cached = withContext(Dispatchers.IO) {
            userDao.getById(id)
        }
        return if (cached != null) {
            AppLogger.w("UserRepository", "Returning cached user $id")
            Result.success(cached.toResponse())
        } else {
            AppLogger.e("UserRepository", "No cached user $id available")
            Result.failure(Exception("No internet connection and no cached user $id"))
        }
    }

    // Extension function: maps API response to DB entity
    fun UserResponse.toEntity() = UserEntity(
        id = id,
        name = name,
        username = username,
        email = email,
        street = address.street,
        suite = address.suite,
        city = address.city,
        zipcode = address.zipcode,
        lat = address.geo.lat,
        lng = address.geo.lng,
        phone = phone,
        website = website,
        companyName = company.name,
        companyCatchPhrase = company.catchPhrase,
        companyBs = company.bs
    )

    // Extension function: maps DB entity back to API response model
    fun UserEntity.toResponse() = UserResponse(
        id = id,
        name = name,
        username = username,
        email = email,
        address = Address(
            street = street,
            suite = suite,
            city = city,
            zipcode = zipcode,
            geo = Geo(lat = lat, lng = lng)
        ),
        phone = phone,
        website = website,
        company = Company(
            name = companyName,
            catchPhrase = companyCatchPhrase,
            bs = companyBs
        )
    )
}




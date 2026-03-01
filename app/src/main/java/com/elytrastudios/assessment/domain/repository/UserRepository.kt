package com.elytrastudios.assessment.domain.repository

import com.elytrastudios.assessment.data.model.UserResponse

interface UserRepository {
    suspend fun getUsers(): Result<List<UserResponse>>
    suspend fun getUser(id: Int): Result<UserResponse>
}

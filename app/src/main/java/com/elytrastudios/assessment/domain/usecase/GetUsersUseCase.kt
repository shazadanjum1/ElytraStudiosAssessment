package com.elytrastudios.assessment.domain.usecase

import com.elytrastudios.assessment.domain.repository.UserRepository
import com.elytrastudios.assessment.data.model.UserResponse
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): Result<List<UserResponse>> = repository.getUsers()

    suspend operator fun invoke(id: Int): Result<UserResponse> = repository.getUser(id)
}

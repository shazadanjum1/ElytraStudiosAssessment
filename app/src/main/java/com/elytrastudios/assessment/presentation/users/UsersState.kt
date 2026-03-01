package com.elytrastudios.assessment.presentation.users

import com.elytrastudios.assessment.data.model.UserResponse


sealed class UsersState {
    object Loading : UsersState()
    data class Success(val users: List<UserResponse>) : UsersState()
    data class Error(val message: String) : UsersState()
}

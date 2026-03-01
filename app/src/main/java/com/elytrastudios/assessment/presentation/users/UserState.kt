package com.elytrastudios.assessment.presentation.users

import com.elytrastudios.assessment.data.model.UserResponse


sealed class UserState {
    object Loading : UserState()
    data class Success(val user: UserResponse) : UserState()
    data class Error(val message: String) : UserState()
}

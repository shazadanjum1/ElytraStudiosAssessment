package com.elytrastudios.assessment.presentation.users


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elytrastudios.assessment.domain.usecase.GetUsersUseCase
import com.elytrastudios.assessment.util.AppLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


@HiltViewModel
class UsersViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
) : ViewModel() {

    private val _usersState = MutableStateFlow<UsersState>(UsersState.Loading)
    val usersState: StateFlow<UsersState> = _usersState

    private val _userState = MutableStateFlow<UserState>(UserState.Loading)
    val userState: StateFlow<UserState> = _userState

    // Fetch all users
    fun fetchUsers() {
        viewModelScope.launch {
            _usersState.value = UsersState.Loading
            val result = getUsersUseCase()
            _usersState.value = result.fold(
                onSuccess = {
                    AppLogger.d("UsersViewModel", "users fetched")
                    UsersState.Success(it)
                },
                onFailure = {
                    AppLogger.d("UsersViewModel", " failed to fetch users")
                    UsersState.Error(it.message ?: "Unknown error")
                }
            )
        }
    }

    // Fetch single user by ID
    fun fetchUser(id: Int) {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            val result = getUsersUseCase(id)
            _userState.value = result.fold(
                onSuccess = {
                    AppLogger.d("UsersViewModel", "user fetched")
                    UserState.Success(it)
                },
                onFailure = {
                    AppLogger.d("UsersViewModel", " failed to fetch user")
                    UserState.Error(it.message ?: "Unknown error")
                }
            )
        }
    }
}


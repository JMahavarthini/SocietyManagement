package com.horizon.societyapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horizon.societyapp.data.local.entity.UserEntity
import com.horizon.societyapp.data.model.Role
import com.horizon.societyapp.data.repository.AuthRepository
import com.horizon.societyapp.data.repository.SessionManager
import com.horizon.societyapp.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _authState = MutableStateFlow<Resource<UserEntity>>(Resource.Empty)
    val authState: StateFlow<Resource<UserEntity>> = _authState.asStateFlow()

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    /** Checks DataStore for a saved session and restores it, so users stay logged in. */
    fun restoreSession() {
        viewModelScope.launch {
            val email = sessionManager.loggedInUserEmail.first()
            if (email != null) {
                val user = authRepository.getUserByEmail(email)
                _currentUser.value = user
            }
        }
    }

    fun register(
        name: String,
        email: String,
        password: String,
        flatNumber: String,
        phone: String,
        role: Role
    ) {
        if (name.isBlank() || email.isBlank() || password.isBlank() || flatNumber.isBlank()) {
            _authState.value = Resource.Error("Please fill in all required fields.")
            return
        }
        _authState.value = Resource.Loading
        viewModelScope.launch {
            val result = authRepository.register(name, email, password, flatNumber, phone, role)
            result.onSuccess {
                // Auto-login right after successful registration for a smoother flow.
                login(email, password)
            }.onFailure {
                _authState.value = Resource.Error(it.message ?: "Registration failed.")
            }
        }
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = Resource.Error("Please enter both email and password.")
            return
        }
        _authState.value = Resource.Loading
        viewModelScope.launch {
            val result = authRepository.login(email, password)
            result.onSuccess { user ->
                sessionManager.saveSession(user.id, user.email)
                _currentUser.value = user
                _authState.value = Resource.Success(user)
            }.onFailure {
                _authState.value = Resource.Error(it.message ?: "Login failed.")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            sessionManager.clearSession()
            _currentUser.value = null
            _authState.value = Resource.Empty
        }
    }

    fun clearError() {
        if (_authState.value is Resource.Error) {
            _authState.value = Resource.Empty
        }
    }
}

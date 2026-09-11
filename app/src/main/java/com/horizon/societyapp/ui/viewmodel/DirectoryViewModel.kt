package com.horizon.societyapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horizon.societyapp.data.local.entity.UserEntity
import com.horizon.societyapp.data.repository.AuthRepository
import com.horizon.societyapp.data.repository.SessionManager
import com.horizon.societyapp.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class DirectoryViewModel(
    private val authRepository: AuthRepository,
    @Suppress("unused") private val sessionManager: SessionManager
) : ViewModel() {

    private val _members = MutableStateFlow<Resource<List<UserEntity>>>(Resource.Loading)
    val members: StateFlow<Resource<List<UserEntity>>> = _members.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.getAllUsers()
                .onEach { list ->
                    _members.value = if (list.isEmpty()) Resource.Empty else Resource.Success(list)
                }
                .catch { e ->
                    _members.value = Resource.Error(e.message ?: "Failed to load member directory.")
                }
                .collect{}
        }
    }
}

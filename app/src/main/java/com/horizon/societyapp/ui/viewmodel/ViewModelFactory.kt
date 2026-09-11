package com.horizon.societyapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.horizon.societyapp.data.repository.AnnouncementRepository
import com.horizon.societyapp.data.repository.AuthRepository
import com.horizon.societyapp.data.repository.ComplaintRepository
import com.horizon.societyapp.data.repository.SessionManager

/**
 * Manually wires repositories into ViewModels. Kept simple and explicit
 * rather than pulling in a DI framework for a project of this scope.
 */
class ViewModelFactory(
    private val authRepository: AuthRepository,
    private val announcementRepository: AnnouncementRepository,
    private val complaintRepository: ComplaintRepository,
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) ->
                AuthViewModel(authRepository, sessionManager) as T

            modelClass.isAssignableFrom(AnnouncementViewModel::class.java) ->
                AnnouncementViewModel(announcementRepository) as T

            modelClass.isAssignableFrom(ComplaintViewModel::class.java) ->
                ComplaintViewModel(complaintRepository) as T

            modelClass.isAssignableFrom(DirectoryViewModel::class.java) ->
                DirectoryViewModel(authRepository, sessionManager) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}

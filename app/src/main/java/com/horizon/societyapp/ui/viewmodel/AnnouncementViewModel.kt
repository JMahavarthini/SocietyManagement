package com.horizon.societyapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horizon.societyapp.data.local.entity.AnnouncementEntity
import com.horizon.societyapp.data.repository.AnnouncementRepository
import com.horizon.societyapp.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AnnouncementViewModel(
    private val repository: AnnouncementRepository
) : ViewModel() {

    private val _announcements = MutableStateFlow<Resource<List<AnnouncementEntity>>>(Resource.Loading)
    val announcements: StateFlow<Resource<List<AnnouncementEntity>>> = _announcements.asStateFlow()

    private val _postState = MutableStateFlow<Resource<Unit>>(Resource.Empty)
    val postState: StateFlow<Resource<Unit>> = _postState.asStateFlow()

    init {
        loadAnnouncements()
    }

    fun loadAnnouncements() {
        viewModelScope.launch {
            repository.getAnnouncements()
                .onEach { list ->
                    _announcements.value = if (list.isEmpty()) Resource.Empty else Resource.Success(list)
                }
                .catch { e ->
                    _announcements.value = Resource.Error(e.message ?: "Failed to load announcements.")
                }
                .collect{}
        }
    }

    fun postAnnouncement(title: String, description: String, postedBy: String) {
        if (title.isBlank() || description.isBlank()) {
            _postState.value = Resource.Error("Title and description are required.")
            return
        }
        _postState.value = Resource.Loading
        viewModelScope.launch {
            try {
                repository.postAnnouncement(title, description, postedBy)
                _postState.value = Resource.Success(Unit)
            } catch (e: Exception) {
                _postState.value = Resource.Error(e.message ?: "Failed to post announcement.")
            }
        }
    }

    fun deleteAnnouncement(announcement: AnnouncementEntity) {
        viewModelScope.launch {
            repository.deleteAnnouncement(announcement)
        }
    }

    fun resetPostState() {
        _postState.value = Resource.Empty
    }
}

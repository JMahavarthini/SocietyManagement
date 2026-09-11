package com.horizon.societyapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horizon.societyapp.data.local.entity.ComplaintEntity
import com.horizon.societyapp.data.model.ComplaintStatus
import com.horizon.societyapp.data.repository.ComplaintRepository
import com.horizon.societyapp.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ComplaintViewModel(
    private val repository: ComplaintRepository
) : ViewModel() {

    private val _complaints = MutableStateFlow<Resource<List<ComplaintEntity>>>(Resource.Loading)
    val complaints: StateFlow<Resource<List<ComplaintEntity>>> = _complaints.asStateFlow()

    private val _submitState = MutableStateFlow<Resource<Unit>>(Resource.Empty)
    val submitState: StateFlow<Resource<Unit>> = _submitState.asStateFlow()

    /**
     * Loads complaints scoped to the signed-in user's role:
     * admins see every complaint in the society, residents see only their own.
     */
    fun loadComplaints(isAdmin: Boolean, flatNumber: String) {
        viewModelScope.launch {
            val flow = if (isAdmin) repository.getAllComplaints() else repository.getComplaintsForResident(flatNumber)
            flow
                .onEach { list ->
                    _complaints.value = if (list.isEmpty()) Resource.Empty else Resource.Success(list)
                }
                .catch { e ->
                    _complaints.value = Resource.Error(e.message ?: "Failed to load complaints.")
                }
                .collect{}
        }
    }

    fun raiseComplaint(
        title: String,
        description: String,
        category: String,
        raisedByName: String,
        raisedByFlat: String
    ) {
        if (title.isBlank() || description.isBlank()) {
            _submitState.value = Resource.Error("Title and description are required.")
            return
        }
        _submitState.value = Resource.Loading
        viewModelScope.launch {
            try {
                repository.raiseComplaint(title, description, category, raisedByName, raisedByFlat)
                _submitState.value = Resource.Success(Unit)
            } catch (e: Exception) {
                _submitState.value = Resource.Error(e.message ?: "Failed to submit complaint.")
            }
        }
    }

    fun updateStatus(complaint: ComplaintEntity, newStatus: ComplaintStatus) {
        viewModelScope.launch {
            repository.updateStatus(complaint, newStatus)
        }
    }

    fun resetSubmitState() {
        _submitState.value = Resource.Empty
    }
}

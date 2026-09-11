package com.horizon.societyapp.data.repository

import com.horizon.societyapp.data.local.dao.ComplaintDao
import com.horizon.societyapp.data.local.entity.ComplaintEntity
import com.horizon.societyapp.data.model.ComplaintStatus
import kotlinx.coroutines.flow.Flow

class ComplaintRepository(private val dao: ComplaintDao) {

    fun getAllComplaints(): Flow<List<ComplaintEntity>> = dao.getAllComplaints()

    fun getComplaintsForResident(flatNumber: String): Flow<List<ComplaintEntity>> =
        dao.getComplaintsForResident(flatNumber)

    suspend fun raiseComplaint(
        title: String,
        description: String,
        category: String,
        raisedByName: String,
        raisedByFlat: String
    ) {
        dao.insert(
            ComplaintEntity(
                title = title,
                description = description,
                category = category,
                raisedByName = raisedByName,
                raisedByFlat = raisedByFlat,
                status = ComplaintStatus.OPEN,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    suspend fun updateStatus(complaint: ComplaintEntity, newStatus: ComplaintStatus) {
        dao.update(complaint.copy(status = newStatus))
    }
}

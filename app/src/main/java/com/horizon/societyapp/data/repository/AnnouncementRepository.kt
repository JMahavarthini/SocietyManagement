package com.horizon.societyapp.data.repository

import com.horizon.societyapp.data.local.dao.AnnouncementDao
import com.horizon.societyapp.data.local.entity.AnnouncementEntity
import kotlinx.coroutines.flow.Flow

class AnnouncementRepository(private val dao: AnnouncementDao) {

    fun getAnnouncements(): Flow<List<AnnouncementEntity>> = dao.getAllAnnouncements()

    suspend fun postAnnouncement(title: String, description: String, postedBy: String) {
        dao.insert(
            AnnouncementEntity(
                title = title,
                description = description,
                postedBy = postedBy,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    suspend fun deleteAnnouncement(announcement: AnnouncementEntity) {
        dao.delete(announcement)
    }
}

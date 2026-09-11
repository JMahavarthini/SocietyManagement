package com.horizon.societyapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.horizon.societyapp.data.local.entity.AnnouncementEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnnouncementDao {

    @Insert
    suspend fun insert(announcement: AnnouncementEntity)

    @Delete
    suspend fun delete(announcement: AnnouncementEntity)

    @Query("SELECT * FROM announcements ORDER BY timestamp DESC")
    fun getAllAnnouncements(): Flow<List<AnnouncementEntity>>
}

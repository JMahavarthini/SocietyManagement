package com.horizon.societyapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.horizon.societyapp.data.local.entity.ComplaintEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ComplaintDao {

    @Insert
    suspend fun insert(complaint: ComplaintEntity)

    @Update
    suspend fun update(complaint: ComplaintEntity)

    @Query("SELECT * FROM complaints ORDER BY timestamp DESC")
    fun getAllComplaints(): Flow<List<ComplaintEntity>>

    @Query("SELECT * FROM complaints WHERE raisedByFlat = :flatNumber ORDER BY timestamp DESC")
    fun getComplaintsForResident(flatNumber: String): Flow<List<ComplaintEntity>>
}

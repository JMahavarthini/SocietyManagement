package com.horizon.societyapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.horizon.societyapp.data.model.ComplaintStatus

@Entity(tableName = "complaints")
data class ComplaintEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val category: String,
    val raisedByName: String,
    val raisedByFlat: String,
    val status: ComplaintStatus,
    val timestamp: Long
)

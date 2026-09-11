package com.horizon.societyapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.horizon.societyapp.data.model.Role

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val email: String,
    val passwordHash: String,
    val flatNumber: String,
    val phone: String,
    val role: Role
)

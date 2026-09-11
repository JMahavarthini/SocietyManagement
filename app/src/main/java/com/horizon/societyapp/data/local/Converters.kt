package com.horizon.societyapp.data.local

import androidx.room.TypeConverter
import com.horizon.societyapp.data.model.ComplaintStatus
import com.horizon.societyapp.data.model.Role

/**
 * Room cannot persist enums natively, so we store them as their String name
 * and convert back when reading. Keeping this centralized avoids repeating
 * the same conversion logic across entities.
 */
class Converters {

    @TypeConverter
    fun fromRole(role: Role): String = role.name

    @TypeConverter
    fun toRole(value: String): Role = Role.valueOf(value)

    @TypeConverter
    fun fromComplaintStatus(status: ComplaintStatus): String = status.name

    @TypeConverter
    fun toComplaintStatus(value: String): ComplaintStatus = ComplaintStatus.valueOf(value)
}

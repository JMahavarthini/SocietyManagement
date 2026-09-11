package com.horizon.societyapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.horizon.societyapp.data.local.dao.AnnouncementDao
import com.horizon.societyapp.data.local.dao.ComplaintDao
import com.horizon.societyapp.data.local.dao.UserDao
import com.horizon.societyapp.data.local.entity.AnnouncementEntity
import com.horizon.societyapp.data.local.entity.ComplaintEntity
import com.horizon.societyapp.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, AnnouncementEntity::class, ComplaintEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun announcementDao(): AnnouncementDao
    abstract fun complaintDao(): ComplaintDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "society_app_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

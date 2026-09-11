package com.horizon.societyapp

import android.app.Application
import com.horizon.societyapp.data.local.AppDatabase
import com.horizon.societyapp.data.repository.AnnouncementRepository
import com.horizon.societyapp.data.repository.AuthRepository
import com.horizon.societyapp.data.repository.ComplaintRepository
import com.horizon.societyapp.data.repository.SessionManager

/**
 * Acts as a minimal manual dependency container. For a project of this size,
 * introducing Hilt/Dagger would add build complexity without real benefit -
 * this keeps dependency wiring explicit, visible, and easy to explain.
 */
class SocietyApplication : Application() {

    private val database by lazy { AppDatabase.getInstance(this) }

    val authRepository by lazy { AuthRepository(database.userDao()) }
    val announcementRepository by lazy { AnnouncementRepository(database.announcementDao()) }
    val complaintRepository by lazy { ComplaintRepository(database.complaintDao()) }
    val sessionManager by lazy { SessionManager(this) }
}

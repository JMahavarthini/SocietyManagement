package com.horizon.societyapp.data.model

/**
 * Defines the two user roles supported by the app.
 * Admins can post announcements and resolve complaints.
 * Residents can view announcements, raise complaints, and browse the directory.
 */
enum class Role {
    ADMIN,
    RESIDENT
}

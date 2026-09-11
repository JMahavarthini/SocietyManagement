package com.horizon.societyapp.data.repository

import com.horizon.societyapp.data.local.dao.UserDao
import com.horizon.societyapp.data.local.entity.UserEntity
import com.horizon.societyapp.data.model.Role
import kotlinx.coroutines.flow.Flow
import java.security.MessageDigest

/**
 * Handles registration and login against the local Room database.
 * Passwords are stored as SHA-256 hashes rather than plain text -
 * a minimal but meaningful security practice even for a local-only demo app.
 */
class AuthRepository(private val userDao: UserDao) {

    suspend fun register(
        name: String,
        email: String,
        password: String,
        flatNumber: String,
        phone: String,
        role: Role
    ): Result<Unit> {
        return try {
            if (userDao.countByEmail(email) > 0) {
                return Result.failure(IllegalStateException("An account with this email already exists."))
            }
            userDao.insertUser(
                UserEntity(
                    name = name,
                    email = email,
                    passwordHash = hash(password),
                    flatNumber = flatNumber,
                    phone = phone,
                    role = role
                )
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<UserEntity> {
        return try {
            val user = userDao.getUserByEmail(email)
                ?: return Result.failure(IllegalStateException("No account found for this email."))
            if (user.passwordHash != hash(password)) {
                return Result.failure(IllegalStateException("Incorrect password."))
            }
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getAllUsers(): Flow<List<UserEntity>> = userDao.getAllUsers()

    suspend fun getUserByEmail(email: String): UserEntity? = userDao.getUserByEmail(email)

    private fun hash(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}

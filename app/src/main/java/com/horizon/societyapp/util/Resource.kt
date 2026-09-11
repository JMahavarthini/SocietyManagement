package com.horizon.societyapp.util

/**
 * Generic wrapper used across ViewModels to represent the state of an
 * asynchronous operation: Loading, Success, Error, or Empty.
 * Screens observe this to render the appropriate UI (spinner, content,
 * error message with retry, or an empty-state illustration/message).
 */
sealed class Resource<out T> {
    data object Loading : Resource<Nothing>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()
    data object Empty : Resource<Nothing>()
}

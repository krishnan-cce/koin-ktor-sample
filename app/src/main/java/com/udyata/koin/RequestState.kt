package com.udyata.koin

sealed class RequestState<out T> {
    data object Idle:RequestState<Nothing>()
    data object Loading:RequestState<Nothing>()
    data class Success<out T>(val data:T):RequestState<T>()
    data class Error(val message:String):RequestState<Nothing>()

    fun isLoading():Boolean = this is Loading
    fun isError():Boolean = this is Error
    fun isSuccess():Boolean = this is Success

    fun getSuccessData() = (this as Success).data

    fun getSuccessDataOrNull():T? {
        return try {
            (this as Success).data
        } catch (e:Exception) {
            null
        }
    }
    fun getErrorMessage() = (this as Error).message
    fun getErrorMessageOrNull():String? {
        return try {
            (this as Error).message
        } catch (e:Exception) {
            null
        }
    }
}
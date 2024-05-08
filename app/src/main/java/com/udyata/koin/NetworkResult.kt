package com.udyata.koin

sealed class NetworkResult<out T> {
    data class Success<T>(val result: T) : NetworkResult<T>()
    data class Error<Nothing>(val body: String?, val exception: NetworkException) :
        NetworkResult<Nothing>()
}


fun NetworkResult.Error<*>.toResourceError(): Resource.Error {
    return when (exception) {
        is NetworkException.NotFoundException -> Resource.Error(ResourceError.SERVICE_UNAVAILABLE)
        is NetworkException.UnauthorizedException -> Resource.Error(ResourceError.UNAUTHORIZED)
        is NetworkException.UnknownException -> Resource.Error(ResourceError.UNKNOWN)
    }
}

sealed class Resource<out T> {
    data class Error(val e: ResourceError) : Resource<Nothing>()
    data class Success<R>(val result: R) : Resource<R>()
}

enum class ResourceError {
    UNAUTHORIZED,
    SERVICE_UNAVAILABLE,
    UNKNOWN,
}

sealed class NetworkException(message: String, cause: Throwable) : Exception(message, cause) {

    data class UnauthorizedException(override val message: String, override val cause: Throwable) :
        NetworkException(message, cause)

    data class NotFoundException(override val message: String, override val cause: Throwable) :
        NetworkException(message, cause)

    data class UnknownException(override val message: String, override val cause: Throwable) :
        NetworkException(message, cause)
}

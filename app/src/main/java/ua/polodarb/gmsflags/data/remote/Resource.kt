package ua.polodarb.gmsflags.data.remote

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(exception: Exception? = null, data: T? = null) :
        Resource<T>(data, exception?.message)
}

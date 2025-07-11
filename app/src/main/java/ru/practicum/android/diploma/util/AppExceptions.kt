package ru.practicum.android.diploma.util

sealed class AppException : Exception() {
    class NoInternetConnection : AppException()
    class EmptyResult : AppException()
    class UnknownException : AppException()
    class ServerException : AppException()
}

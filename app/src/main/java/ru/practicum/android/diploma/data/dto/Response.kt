package ru.practicum.android.diploma.data.dto

open class Response {
    var resultCode = 0
}

class DataResponse<T>(
    val data: T
) : Response()

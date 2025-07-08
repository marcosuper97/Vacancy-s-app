package ru.practicum.android.diploma.data.network

import android.content.Context
import ru.practicum.android.diploma.data.dto.Response

class RetrofitNetworkClient(
    private val context: Context,
    private val apiService: HhApiService
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        return Response() // На время
    }

}


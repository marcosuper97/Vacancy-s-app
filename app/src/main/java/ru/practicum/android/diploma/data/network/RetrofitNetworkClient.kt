package ru.practicum.android.diploma.data.network

import android.content.Context
import android.util.Log
import retrofit2.HttpException
import ru.practicum.android.diploma.data.dto.VacancyDetailsResponse
import ru.practicum.android.diploma.data.dto.search.VacancySearchResponseDto
import ru.practicum.android.diploma.util.AppException
import ru.practicum.android.diploma.util.isInternetAvailable
import java.io.IOException

class RetrofitNetworkClient(
    private val context: Context,
    private val apiService: HhApiService
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Result<VacancyDetailsResponse> {
        return Result.success(VacancyDetailsResponse("ВОТ ЭТО ДАААА")) // На время
    }

    override suspend fun vacanciesSearchRequest(requestQuery: Map<String, String>): Result<VacancySearchResponseDto> {
        return try {
            if (!isInternetAvailable(context)) {
                return Result.failure(AppException.NoInternetConnection())
            }
            val response = apiService.searchVacancies(requestQuery)
            Result.success(response)
        } catch (e: IOException) {
            Log.d("Exception", e.cause.toString())
            Result.failure(AppException.NoInternetConnection())
        } catch (e: HttpException) {
            Log.d("Exception", e.cause.toString())
            when (e.code()) {
                NOT_FOUND_CODE -> {
                    Result.failure(AppException.NotFound())
                }

                in SERVER_ERROR_CODE_MIN..SERVER_ERROR_CODE_MAX -> {
                    Result.failure(AppException.ServerError())
                }

                else -> Result.failure(AppException.UnknownException())
            }
        }
    }

    companion object {
        private const val NOT_FOUND_CODE = 404
        private const val SERVER_ERROR_CODE_MIN = 500
        private const val SERVER_ERROR_CODE_MAX = 599
    }

}


package ru.practicum.android.diploma.data.network

import android.content.Context
import retrofit2.HttpException
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.search.VacancySearchResponseDto
import ru.practicum.android.diploma.util.AppException
import ru.practicum.android.diploma.util.isInternetAvailable
import java.io.IOException

class RetrofitNetworkClient(
    private val context: Context,
    private val apiService: HhApiService
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        return Response() // На время
    }

    override suspend fun vacanciesSearchRequest(requestQuery: Map<String, String>): Result<VacancySearchResponseDto> {
        return try {
            if (!isInternetAvailable(context)) {
                return Result.failure(AppException.NoInternetConnection())
            }
            val response = apiService.searchVacancies(requestQuery)
            return Result.success(response)
        } catch (e: IOException) {
            Result.failure(AppException.NoInternetConnection())
        } catch (e: HttpException) {
            return when(e.code()){
                404 -> Result.failure(AppException.NotFound())
                in 500..599 -> Result.failure(AppException.ServerError())
                else -> Result.failure(AppException.UnknownException())
            }
        }
    }

}


package ru.practicum.android.diploma.data.network

import android.content.Context
import retrofit2.HttpException
import ru.practicum.android.diploma.data.dto.AreasRequest
import ru.practicum.android.diploma.data.dto.DataResponse
import ru.practicum.android.diploma.data.dto.IndustriesRequest
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.VacancyDetailsRequest
import ru.practicum.android.diploma.data.dto.VacancySearchRequest
import ru.practicum.android.diploma.util.isInternetAvailable
import java.io.IOException
import java.net.SocketTimeoutException

class RetrofitNetworkClient(
    private val context: Context,
    private val apiService: HhApiService
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (!isInternetAvailable(context)) return Response().apply { resultCode = -1 }

        return when (dto) {
            is VacancySearchRequest -> apiCall(
                call = {
                    apiService.search(
                        text = dto.text,
                        page = dto.page,
                        perPage = dto.perPage,
                        area = dto.area,
                        industry = dto.industry,
                        onlyWithSalary = dto.onlyWithSalary
                    )
                },
                errorMessage = "Ошибка поиска вакансий"
            )

            is VacancyDetailsRequest -> apiCall(
                call = { apiService.getVacancyDetails(dto.vacancyId) },
                errorMessage = "Ошибка получения вакансии"
            )

            is IndustriesRequest -> apiCall(
                call = {
                    val response = apiService.getIndustries()
                    DataResponse(response)
                },
                errorMessage = "Ошибка получения отраслей"
            )

            is AreasRequest -> apiCall(
                call = {
                    val response = apiService.getAreas()
                    DataResponse(response)
                },
                errorMessage = "Ошибка получения регионов"
            )

            else -> Response().apply {
                resultCode = 400
                errorMessage = "Неизвестный тип запроса"
            }
        }
    }

    private fun handleApiError(e: Exception, defaultMessage: String): Response {
        return when (e) {
            is HttpException -> {
                val errorBody = e.response()?.errorBody()?.string()
                val message = errorBody?.takeIf { it.isNotBlank() }
                    ?: when (e.code()) {
                        400 -> "Неверные параметры запроса"
                        401 -> "Ошибка авторизации"
                        403 -> "Доступ запрещен"
                        404 -> "Ресурс не найден"
                        429 -> "Слишком много запросов"
                        else -> "Ошибка сервера (${e.code()})"
                    }

                Response().apply {
                    resultCode = e.code()
                    errorMessage = "$defaultMessage: $message"
                }
            }

            is SocketTimeoutException -> Response().apply {
                resultCode = 504
                errorMessage = "$defaultMessage: Таймаут соединения"
            }

            is IOException -> Response().apply {
                resultCode = 503
                errorMessage = "$defaultMessage: Сетевая ошибка"
            }

            else -> Response().apply {
                resultCode = 500
                errorMessage = "$defaultMessage: Неизвестная ошибка"
            }
        }
    }

    private inline fun <T> apiCall(
        call: () -> T,
        successAction: T.() -> Unit = {},
        errorMessage: String
    ): Response {
        return try {
            val result = call()
            successAction(result)

            when (result) {
                is Response -> result.apply {
                    if (resultCode == 0) resultCode = 200
                }
                else -> DataResponse(result).apply {
                    resultCode = 200
                }
            }
        } catch (e: Exception) {
            handleApiError(e, errorMessage)
        }
    }

}

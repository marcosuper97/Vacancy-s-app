package ru.practicum.android.diploma.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import retrofit2.HttpException
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.VacancyDetailsRequest
import ru.practicum.android.diploma.data.dto.VacancySearchRequest

class RetrofitNetworkClient(
    private val context: Context,
    private val apiService: HhApiService
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) return Response().apply { resultCode = -1 }

        return when (dto) {
            is VacancySearchRequest -> apiCall(
                call = {
                    apiService.search(
                        text = dto.text,
                        page = dto.page,
                        perPage = dto.perPage,
                        area = dto.area,
                        industry = dto.industry
                    )
                },
                errorMessage = "Ошибка поиска вакансий"
            )

            is VacancyDetailsRequest -> apiCall(
                call = { apiService.getVacancyDetails(dto.vacancyId) },
                errorMessage = "Ошибка получения вакансии"
            )

            else -> Response().apply {
                resultCode = 400
                errorMessage = "Неизвестный тип запроса"
            }
        }
    }


    ///
    private fun handleApiError(e: Exception, defaultMessage: String): Response {
        return when (e) {
            is HttpException -> {
                val (code, message) = when (e.code()) {
                    400 -> 400 to "$defaultMessage: Параметры переданы с ошибкой"
                    403 -> 403 to "$defaultMessage: Требуется ввести капчу"
                    404 -> 404 to "$defaultMessage: Указанная вакансия не существует или у пользователя нет прав на просмотр вакансии"
                    429 -> 429 to "$defaultMessage: Слишком много запросов"
                    else -> e.code() to "$defaultMessage: Ошибка сервера (${e.code()})"
                }
                Response().apply {
                    resultCode = code
                    errorMessage = message
                }
            }
            else -> Response().apply {
                resultCode = 500
                errorMessage = "$defaultMessage: ${e.message ?: "Неизвестная ошибка"}"
            }
        }
    }

    private inline fun <T> apiCall(
        call: () -> T,
        successAction: T.() -> Unit = {},
        errorMessage: String
    ): Response {
        return try {
            val result = call() // Выполняем API-вызов
            successAction(result) // Применяем дополнительное действие
            when (result) {
                is Response -> result.apply { resultCode = 200 }
                else -> Response().apply {
                    resultCode = 200
                    // Можно добавить данные из result, если нужно:
                    // data = result
                }
            }
        } catch (e: Exception) {
            handleApiError(e, errorMessage)
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}

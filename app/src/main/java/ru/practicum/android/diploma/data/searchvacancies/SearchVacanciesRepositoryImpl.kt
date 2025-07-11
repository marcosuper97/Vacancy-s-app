package ru.practicum.android.diploma.data.searchvacancies

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.data.dto.search.VacancySearchRequest
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.models.VacanciesList
import ru.practicum.android.diploma.domain.searchvacancies.SearchVacanciesRepository

class SearchVacanciesRepositoryImpl(
    private val networkClient: NetworkClient
) : SearchVacanciesRepository {


    override fun doRequest(textRequest: String, page: Int): Flow<Result<VacanciesList>> = flow {
        withContext(Dispatchers.IO) {
            val response = networkClient.vacanciesSearchRequest(createRequest(textRequest,page))

            when{
                response.isSuccess ->{
                    val data = response.getOrNull()
                    data?.let {  }
                }
                response.isFailure -> {

                }
            }
        }
    }

    override fun createRequest(textRequest: String, page: Int): Map<String, String> {
        return VacancySearchRequest(
            page = page,
            text = textRequest,
            area = null,
            industry = null,
            salary = null,
            onlyWithSalary = null
        ).run {
            toQueryMap()
        }
    }
}

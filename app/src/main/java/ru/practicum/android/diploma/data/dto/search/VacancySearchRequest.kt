package ru.practicum.android.diploma.data.dto.search

data class VacancySearchRequest(
    val page: Int,
    val perPage: String = "20",
    val text: String,
    val area: String?,
    val industry: String?,
    val salary: Long?,
    val onlyWithSalary: Boolean?
) {
    fun toQueryMap(): Map<String, String> = buildMap {
        put("page", page.toString())
        put("per_page", perPage)
        put("text", text)
        area?.let { put("area", area) }
        industry?.let { put("industry", industry) }
        salary?.let { put("salary", salary.toString()) }
        if (onlyWithSalary == true) {
            put("only_with_salary", "true")
        }
    }
}

package ru.practicum.android.diploma.util

import android.content.Context
import ru.practicum.android.diploma.R

fun formatSalary(context: Context, salaryFrom: String?, salaryTo: String?, currency: String?): String {
    return when {
        salaryFrom.isNullOrEmpty() && salaryTo.isNullOrEmpty() -> context.getString(R.string.expected_salary)
        !salaryFrom.isNullOrEmpty() && salaryTo.isNullOrEmpty() -> "${context.getString(R.string.from)} $currency"
        salaryFrom.isNullOrEmpty() && !salaryTo.isNullOrEmpty() -> "${context.getString(R.string.to)} $currency"
        else -> "${context.getString(R.string.from)} $salaryFrom ${context.getString(R.string.to)} $salaryTo $currency"
    }
}

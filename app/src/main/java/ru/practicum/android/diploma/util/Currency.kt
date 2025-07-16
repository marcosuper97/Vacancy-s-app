package ru.practicum.android.diploma.util
import android.content.Context
import android.util.Log
import ru.practicum.android.diploma.R


enum class Currency(val label: String) {
    USD("$"),
    EUR("€"),
    RUB("₽"),
    RUR("₽"),
    AZN("₼"),
    BYR("br"),
    GEL("₾"),
    KGS("с"),
    KZT("₸"),
    UAH("₴"),
    UZS("so\'m")
}

fun formatSalary(context: Context, salaryFrom: Int?, salaryTo: Int?, currency: String?): String {
    val currencySymbol = currency.formatCurrencySymbol()
    val formattedSalaryFrom = salaryFrom?.formatWithThousandsSeparator() ?: ""
    val formattedSalaryTo = salaryTo?.formatWithThousandsSeparator() ?: ""

    Log.d("formatSalary", "currencySymbol: $currencySymbol, formattedSalaryFrom: $formattedSalaryFrom, formattedSalaryTo: $formattedSalaryTo")

    return when {
        salaryFrom == null && salaryTo == null -> context.getString(R.string.salary_not_specified)
        salaryFrom != null && salaryTo == null -> context.getString(
            R.string.from,
            "$formattedSalaryFrom $currencySymbol"
        )
        salaryFrom == null && salaryTo != null -> context.getString(
            R.string.to,
            "$formattedSalaryTo $currencySymbol"
        )
        else -> "${context.getString(R.string.from, "$formattedSalaryFrom")} ${
            context.getString(
                R.string.to,
                "$formattedSalaryTo"
            )
        } $currencySymbol"
    }
}

private fun String?.formatCurrencySymbol(): String {
    val currencySymbol = this.toCurrencySymbol()

    return currencySymbol.ifEmpty {
        this ?: NOTHING
    }
}

fun String?.toCurrencySymbol(): String {
    return this?.let { code ->
        enumValues<Currency>().find { it.name == code }?.label ?: NOTHING
    } ?: NOTHING
}

const val NOTHING: String = ""

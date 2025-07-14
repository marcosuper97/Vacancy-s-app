package ru.practicum.android.diploma.util

enum class Currency(val label: String) {
    USD("$"),
    EUR("€"),
    RUB("₽"),
    AZN("₼"),
    BYR("br"),
    GEL("₾"),
    KGS("с"),
    KZT("₸"),
    UAH("₴"),
    UZS("so\'m")
}

fun String?.toCurrencySymbol(): String? {
    return this?.let { code ->
        enumValues<Currency>().find { it.name == code }?.label
    }
}

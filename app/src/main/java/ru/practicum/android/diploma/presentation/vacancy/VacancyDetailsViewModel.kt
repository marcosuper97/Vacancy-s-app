package ru.practicum.android.diploma.presentation.vacancy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Currency
import ru.practicum.android.diploma.domain.models.Employer
import ru.practicum.android.diploma.domain.models.SalaryRange
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.ui.common.CommandChannel
import ru.practicum.android.diploma.ui.common.UiModel
import kotlin.time.Duration.Companion.seconds

data class VacancyUiState(
    val isFetching: Boolean = false,
    val vacancyDetails: VacancyDetails? = null,
    val isError: Boolean = false
) {
    val items = buildList {
        if (vacancyDetails != null) {
            add(VacancyDetailsItemUiModel.VacancyName(vacancyDetails))
            add(VacancyDetailsItemUiModel.VacancyCompany(vacancyDetails))
            add(VacancyDetailsItemUiModel.VacancyExperience(vacancyDetails))
            add(VacancyDetailsItemUiModel.VacancyDescription(vacancyDetails))
            if (vacancyDetails.keySkills.isNotEmpty()) {
                add(VacancyDetailsItemUiModel.VacancyKeySkills(vacancyDetails))
            }
        }
    }

    val isContentVisible by lazy {
        !isFetching && !isError && items.isNotEmpty()
    }

    val isEmptyVisible by lazy {
        !isFetching && isError
    }
}

sealed class VacancyDetailsCommand {
    data object NavigateBack : VacancyDetailsCommand()
    data class NavigateToShare(
        val url: String
    ) : VacancyDetailsCommand()
}

sealed class VacancyDetailsItemUiModel : UiModel<VacancyDetailsItemUiModel> {
    data class VacancyName(
        private val vacancyDetails: VacancyDetails
    ) : VacancyDetailsItemUiModel() {
        val name = vacancyDetails.name
        val from = vacancyDetails.salaryRange.from
        val to = vacancyDetails.salaryRange.to
        val currency = vacancyDetails.salaryRange.currency

        override fun areItemsTheSame(other: VacancyDetailsItemUiModel) = other is VacancyName
        override fun areContentsTheSame(other: VacancyDetailsItemUiModel) = other is VacancyName
    }

    data class VacancyCompany(
        private val vacancyDetails: VacancyDetails
    ) : VacancyDetailsItemUiModel() {
        val logoUrl = vacancyDetails.employer.logoUrl
        val name = vacancyDetails.employer.name
        val region = vacancyDetails.area.name

        override fun areItemsTheSame(other: VacancyDetailsItemUiModel) = other is VacancyCompany
        override fun areContentsTheSame(other: VacancyDetailsItemUiModel) = other is VacancyCompany
    }

    data class VacancyExperience(
        private val vacancyDetails: VacancyDetails
    ) : VacancyDetailsItemUiModel() {
        val experience = vacancyDetails.experience
        val schedule = "${vacancyDetails.schedule}, ${vacancyDetails.employment}"

        override fun areItemsTheSame(other: VacancyDetailsItemUiModel) = other is VacancyExperience
        override fun areContentsTheSame(other: VacancyDetailsItemUiModel) = other is VacancyExperience
    }

    data class VacancyDescription(
        private val vacancyDetails: VacancyDetails
    ) : VacancyDetailsItemUiModel() {
        val description = vacancyDetails.description

        override fun areItemsTheSame(other: VacancyDetailsItemUiModel) = other is VacancyDescription
        override fun areContentsTheSame(other: VacancyDetailsItemUiModel) = other is VacancyDescription
    }

    data class VacancyKeySkills(
        private val vacancyDetails: VacancyDetails
    ) : VacancyDetailsItemUiModel() {
        val keySkills = vacancyDetails.keySkills

        override fun areItemsTheSame(other: VacancyDetailsItemUiModel) = other is VacancyKeySkills
        override fun areContentsTheSame(other: VacancyDetailsItemUiModel) = other is VacancyKeySkills
    }
}

class VacancyViewModel : ViewModel() {
    val vacancyDetails = VacancyDetails(
        id = "1224348",
        name = "Программист",
        employer = Employer(
            logoUrl = "https://img.hhcdn.ru/employer-logo/2002918.jpeg",
            name = "SIBERS"
        ),
        area = Area(
            id = "11",
            name = "Барнаул"
        ),
        salaryRange = SalaryRange(
            currency = Currency.UZS,
            from = 45000,
            gross = false,
            to = 55000
        ),
        experience = "От 1 года до 3 лет",
        schedule = "Полный день",
        employment = "Полная занятость",
        description = """<p><strong>Заработная плата по результатам собеседования</strong></p>
            | <p><strong>Обязанности:</strong></p> <ul> <li>Администрирование операционных систем MS Windows 7, Windows
            |  10, Linux и серверных операционных систем Windows Server 2008, Windows Server 2019, Альт сервер,
            |   поддержка пользователей ПК, знание офисных программ MS Office, LibreOffice и др.;</li> <li>Навыки
            |    настройки АРМ Заказчика, личного кабинета в ЕИС в сфере закупок, РТС тендер;</li> <li>Работа с 
            |    сетями и маршрутизация: обслуживание локальной сети, резервирование каналов связи, маршрутизация 
            |    подсетей, модернизация существующей сети; контроль Интернет трафика;</li> <li>Работа с аппаратным 
            |    обеспечением ПК и серверов: диагностика неисправностей, модульный ремонт, техническое обслуживание, 
            |    установка драйверов устройств;</li> <li>Знание 1С: администрирование, резервное копирование, поддержка
            |     пользователей, настройка удаленного доступа к базам 1С;</li> <li>Безопасность: работа с ЭЦП и СЗИ, 
            |     обеспечение информационной безопасности при работе в локальной сети учреждения.</li> </ul> 
            |     <p><strong>Требования:</strong></p> <ul> <li>Высшее профессиональное образование;</li>
            |      <li>Опыт работы от 1 года;</li> <li>Приветствуется опыт работы в медицинской организации и знания
            |       специализированных медицинских программ</li> <li>Целеустремленность, обучаемость.</li> </ul>
            |        <strong>Условия:</strong> <ul> <li>Оформление согласно ТК РФ;</li> <li>6 - часовой рабочий день
            |         (с 9.00 до 16.00).</li> <li>Официальная заработная плата два раза в месяц</li>
            |          </ul>""".trimMargin(),
        keySkills = listOf(
            "Администрирование серверов Windows",
            "Linux",
            "Ремонт ПК",
            "Администрирование сетевого оборудования",
            "Windows 7"
        )
    )

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isFetching = true)
            }
            delay(2.seconds)
            _uiState.update { prefState ->
                prefState.copy(
                    isFetching = false,
//                    isError = true,
                    vacancyDetails = vacancyDetails
                )
            }
        }
    }

    private val _uiState = MutableStateFlow(VacancyUiState())
    val uiState = _uiState.asStateFlow()

    private val _commands = CommandChannel<VacancyDetailsCommand>()
    val commands = _commands.receiveAsFlow()

    fun onBackClicked() {
        viewModelScope.launch {
            _commands.send(VacancyDetailsCommand.NavigateBack)
        }
    }

    fun onShareClick() {
        viewModelScope.launch {
            val vacancyDetails = uiState.value.vacancyDetails
            if (vacancyDetails != null) {
                val url = "https://hh.ru/vacancy/${vacancyDetails.id}"
                _commands.send(VacancyDetailsCommand.NavigateToShare(url))
            }
        }
    }
}

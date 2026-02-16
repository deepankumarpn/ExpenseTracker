package deepankumarpn.github.io.expensetracker.domain.model

import kotlinx.serialization.Serializable

/**
 * Domain model representing user settings.
 * Note: Default currency is USD as a fallback.
 * Actual default is determined by device locale in SettingsRepositoryImpl.
 */
@Serializable
data class UserSettings(
    val currency: CurrencyType = CurrencyType.USD,
    val homeDuration: DurationFilter = DurationFilter.MONTHLY,
    val customDays: Int = 30,
    val customSummaryItems: List<String> = emptyList()
)

package deepankumarpn.github.io.expensetracker.data.repository

import deepankumarpn.github.io.expensetracker.base.StateFullResult
import deepankumarpn.github.io.expensetracker.domain.model.CurrencyType
import deepankumarpn.github.io.expensetracker.domain.model.DurationFilter
import deepankumarpn.github.io.expensetracker.domain.model.UserSettings
import deepankumarpn.github.io.expensetracker.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * In-memory implementation of SettingsRepository.
 */
class SettingsRepositoryImpl : SettingsRepository {
    private val settings = MutableStateFlow(
        UserSettings(
            currency = CurrencyType.getDefaultCurrency(),
            homeDuration = DurationFilter.MONTHLY,
            customDays = 30,
            customSummaryItems = emptyList()
        )
    )

    override fun getUserSettings(): Flow<UserSettings> = settings

    override suspend fun updateCurrency(currency: CurrencyType): StateFullResult<Unit> {
        return try {
            settings.value = settings.value.copy(currency = currency)
            StateFullResult.Success(Unit)
        } catch (e: Exception) {
            StateFullResult.Error(e.message ?: "Failed to update currency")
        }
    }

    override suspend fun updateHomeDuration(duration: DurationFilter): StateFullResult<Unit> {
        return try {
            settings.value = settings.value.copy(homeDuration = duration)
            StateFullResult.Success(Unit)
        } catch (e: Exception) {
            StateFullResult.Error(e.message ?: "Failed to update duration")
        }
    }

    override suspend fun updateCustomDays(days: Int): StateFullResult<Unit> {
        return try {
            settings.value = settings.value.copy(customDays = days)
            StateFullResult.Success(Unit)
        } catch (e: Exception) {
            StateFullResult.Error(e.message ?: "Failed to update custom days")
        }
    }

    override suspend fun addCustomSummaryItem(item: String): StateFullResult<Unit> {
        return try {
            settings.value = settings.value.copy(
                customSummaryItems = settings.value.customSummaryItems + item
            )
            StateFullResult.Success(Unit)
        } catch (e: Exception) {
            StateFullResult.Error(e.message ?: "Failed to add summary item")
        }
    }

    override suspend fun removeCustomSummaryItem(item: String): StateFullResult<Unit> {
        return try {
            settings.value = settings.value.copy(
                customSummaryItems = settings.value.customSummaryItems.filter { it != item }
            )
            StateFullResult.Success(Unit)
        } catch (e: Exception) {
            StateFullResult.Error(e.message ?: "Failed to remove summary item")
        }
    }

    override suspend fun updateSettings(settings: UserSettings): StateFullResult<Unit> {
        return try {
            this.settings.value = settings
            StateFullResult.Success(Unit)
        } catch (e: Exception) {
            StateFullResult.Error(e.message ?: "Failed to update settings")
        }
    }

    suspend fun getSettings(): UserSettings {
        return settings.value
    }
}

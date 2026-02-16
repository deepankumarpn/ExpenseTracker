package deepankumarpn.github.io.expensetracker.domain.repository

import deepankumarpn.github.io.expensetracker.base.StateFullResult
import deepankumarpn.github.io.expensetracker.domain.model.CurrencyType
import deepankumarpn.github.io.expensetracker.domain.model.DurationFilter
import deepankumarpn.github.io.expensetracker.domain.model.UserSettings
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for settings operations.
 */
interface SettingsRepository {
    /**
     * Get user settings.
     */
    fun getUserSettings(): Flow<UserSettings>

    /**
     * Update currency setting.
     */
    suspend fun updateCurrency(currency: CurrencyType): StateFullResult<Unit>

    /**
     * Update home duration setting.
     */
    suspend fun updateHomeDuration(duration: DurationFilter): StateFullResult<Unit>

    /**
     * Update custom days setting.
     */
    suspend fun updateCustomDays(days: Int): StateFullResult<Unit>

    /**
     * Add custom summary item.
     */
    suspend fun addCustomSummaryItem(item: String): StateFullResult<Unit>

    /**
     * Remove custom summary item.
     */
    suspend fun removeCustomSummaryItem(item: String): StateFullResult<Unit>

    /**
     * Update all settings.
     */
    suspend fun updateSettings(settings: UserSettings): StateFullResult<Unit>
}

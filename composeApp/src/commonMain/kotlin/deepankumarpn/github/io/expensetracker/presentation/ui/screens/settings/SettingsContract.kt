package deepankumarpn.github.io.expensetracker.presentation.ui.screens.settings

import deepankumarpn.github.io.expensetracker.base.UiEffect
import deepankumarpn.github.io.expensetracker.base.UiEvent
import deepankumarpn.github.io.expensetracker.base.UiState
import deepankumarpn.github.io.expensetracker.domain.model.Category
import deepankumarpn.github.io.expensetracker.domain.model.CurrencyType
import deepankumarpn.github.io.expensetracker.domain.model.DurationFilter
import deepankumarpn.github.io.expensetracker.domain.model.PaymentType

/**
 * Contract for Settings screen following MVI pattern.
 */
class SettingsContract {
    /**
     * UI Events that can be triggered from the Settings screen.
     */
    sealed class Event : UiEvent {
        // Profile
        data object LoadProfile : Event()

        // Currency
        data class CurrencyChanged(val currency: CurrencyType) : Event()

        // Categories
        data object LoadCategories : Event()
        data class AddCategory(val name: String) : Event()
        data class UpdateCategory(val id: String, val name: String) : Event()
        data class DeleteCategory(val id: String) : Event()

        // Payment Types
        data object LoadPaymentTypes : Event()
        data class AddPaymentType(val name: String) : Event()
        data class UpdatePaymentType(val id: String, val name: String) : Event()
        data class DeletePaymentType(val id: String) : Event()

        // Duration Settings
        data class HomeDurationChanged(val duration: DurationFilter) : Event()
        data class CustomDaysChanged(val days: Int) : Event()

        // Sign Out
        data object SignOut : Event()
    }

    /**
     * UI State for the Settings screen.
     */
    /**
     * UI State for the Settings screen.
     * Note: selectedCurrency defaults to USD as fallback.
     * Actual currency is loaded from settings which detects device locale.
     */
    data class State(
        val userName: String = "",
        val userEmail: String = "",
        val selectedCurrency: CurrencyType = CurrencyType.USD,
        val categories: List<Category> = emptyList(),
        val paymentTypes: List<PaymentType> = emptyList(),
        val categoriesInUse: Set<String> = emptySet(),
        val paymentTypesInUse: Set<String> = emptySet(),
        val homeDuration: DurationFilter = DurationFilter.MONTHLY,
        val customDays: Int = 30,
        val customSummaryItems: List<String> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    ) : UiState

    /**
     * One-time UI effects for the Settings screen.
     */
    sealed class Effect : UiEffect {
        data object NavigateToLogin : Effect()
        data class ShowError(val message: String) : Effect()
        data class ShowDeleteBlockedMessage(val name: String) : Effect()
        data class ShowSuccess(val message: String) : Effect()
    }
}

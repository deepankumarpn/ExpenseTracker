package deepankumarpn.github.io.expensetracker.presentation.ui.screens.home

import deepankumarpn.github.io.expensetracker.base.UiEffect
import deepankumarpn.github.io.expensetracker.base.UiEvent
import deepankumarpn.github.io.expensetracker.base.UiState
import deepankumarpn.github.io.expensetracker.domain.model.Category
import deepankumarpn.github.io.expensetracker.domain.model.CurrencyType
import deepankumarpn.github.io.expensetracker.domain.model.PaymentType
import deepankumarpn.github.io.expensetracker.domain.model.TransactionType
import kotlinx.datetime.LocalDate

/**
 * Contract for Home screen following MVI pattern.
 */
class HomeContract {
    /**
     * UI Events that can be triggered from the Home screen.
     */
    sealed class Event : UiEvent {
        data class TypeSelected(val type: TransactionType) : Event()
        data class AmountChanged(val amount: String) : Event()
        data class NoteChanged(val note: String) : Event()
        data class CategorySelected(val category: Category) : Event()
        data class PaymentTypeSelected(val paymentType: PaymentType) : Event()
        data class DateSelected(val date: LocalDate) : Event()
        data object ShowDatePicker : Event()
        data object DismissDatePicker : Event()
        data object SubmitTransaction : Event()
        data object LoadSummary : Event()
    }

    /**
     * UI State for the Home screen.
     */
    data class State(
        val transactionType: TransactionType = TransactionType.EXPENSE,
        val amount: String = "",
        val note: String = "",
        val selectedCategory: Category? = null,
        val selectedPaymentType: PaymentType? = null,
        val selectedDate: LocalDate? = null,
        val categories: List<Category> = emptyList(),
        val paymentTypes: List<PaymentType> = emptyList(),
        val totalIncome: Double = 0.0,
        val totalExpense: Double = 0.0,
        val durationLabel: String = "This Month",
        val currency: CurrencyType = CurrencyType.getDefaultCurrency(),
        val isLoading: Boolean = false,
        val isSubmitting: Boolean = false,
        val showDatePicker: Boolean = false,
        val error: String? = null
    ) : UiState

    /**
     * One-time UI effects for the Home screen.
     */
    sealed class Effect : UiEffect {
        data object TransactionAdded : Effect()
        data class ShowError(val message: String) : Effect()
        data object ShowDatePickerDialog : Effect()
        data class ShowSuccess(val message: String) : Effect()
    }
}

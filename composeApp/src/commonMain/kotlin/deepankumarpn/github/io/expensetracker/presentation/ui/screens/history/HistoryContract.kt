package deepankumarpn.github.io.expensetracker.presentation.ui.screens.history

import deepankumarpn.github.io.expensetracker.base.UiEffect
import deepankumarpn.github.io.expensetracker.base.UiEvent
import deepankumarpn.github.io.expensetracker.base.UiState
import deepankumarpn.github.io.expensetracker.domain.model.CurrencyType
import deepankumarpn.github.io.expensetracker.domain.model.DurationFilter
import deepankumarpn.github.io.expensetracker.domain.model.PaymentType
import deepankumarpn.github.io.expensetracker.domain.model.Transaction
import deepankumarpn.github.io.expensetracker.domain.model.TransactionType

/**
 * Contract for History screen following MVI pattern.
 */
class HistoryContract {
    /**
     * UI Events that can be triggered from the History screen.
     */
    sealed class Event : UiEvent {
        data class DurationFilterChanged(val filter: DurationFilter) : Event()
        data class YearChanged(val year: Int) : Event()
        data class MonthChanged(val month: Int) : Event()
        data class PaymentTypeFilterChanged(val paymentType: PaymentType?) : Event()
        data class TransactionTypeFilterChanged(val type: TransactionType?) : Event()
        data object LoadTransactions : Event()
        data object LoadMore : Event()
    }

    /**
     * UI State for the History screen.
     */
    data class State(
        val transactions: List<Transaction> = emptyList(),
        val selectedDuration: DurationFilter = DurationFilter.MONTHLY,
        val selectedYear: Int = 2024,
        val selectedMonth: Int = 1,
        val selectedPaymentType: PaymentType? = null,
        val selectedTransactionType: TransactionType? = null,
        val totalIncome: Double = 0.0,
        val totalExpense: Double = 0.0,
        val currency: CurrencyType = CurrencyType.getDefaultCurrency(),
        val isLoading: Boolean = false,
        val error: String? = null
    ) : UiState

    /**
     * One-time UI effects for the History screen.
     */
    sealed class Effect : UiEffect {
        data class ShowError(val message: String) : Effect()
    }
}

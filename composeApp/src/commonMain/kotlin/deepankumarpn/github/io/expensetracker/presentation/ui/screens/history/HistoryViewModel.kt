package deepankumarpn.github.io.expensetracker.presentation.ui.screens.history

import androidx.lifecycle.viewModelScope
import deepankumarpn.github.io.expensetracker.base.BaseViewModel
import deepankumarpn.github.io.expensetracker.domain.model.DurationFilter
import deepankumarpn.github.io.expensetracker.domain.repository.SettingsRepository
import deepankumarpn.github.io.expensetracker.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

/**
 * ViewModel for History screen.
 */
class HistoryViewModel(
    private val transactionRepository: TransactionRepository,
    private val settingsRepository: SettingsRepository
) : BaseViewModel<HistoryContract.State, HistoryContract.Event, HistoryContract.Effect>(
    initialState = HistoryContract.State()
) {

    init {
        loadTransactions()
    }

    override fun onEvent(event: HistoryContract.Event) {
        when (event) {
            is HistoryContract.Event.DurationFilterChanged -> {
                setState { copy(selectedDuration = event.filter) }
                loadTransactions()
            }
            is HistoryContract.Event.YearChanged -> {
                setState { copy(selectedYear = event.year) }
                loadTransactions()
            }
            is HistoryContract.Event.MonthChanged -> {
                setState { copy(selectedMonth = event.month) }
                loadTransactions()
            }
            is HistoryContract.Event.PaymentTypeFilterChanged -> {
                setState { copy(selectedPaymentType = event.paymentType) }
                loadTransactions()
            }
            is HistoryContract.Event.TransactionTypeFilterChanged -> {
                setState { copy(selectedTransactionType = event.type) }
                loadTransactions()
            }
            HistoryContract.Event.LoadTransactions -> loadTransactions()
            HistoryContract.Event.LoadMore -> {
                // TODO: Implement pagination
                loadTransactions()
            }
        }
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }

            combine(
                transactionRepository.getTransactionsByFilter(currentState.selectedDuration),
                settingsRepository.getUserSettings()
            ) { transactions, settings ->
                Pair(transactions, settings)
            }
                .catch { e ->
                    setState { copy(isLoading = false, error = e.message) }
                }
                .collect { (transactions, settings) ->
                    val income = transactions.filter { it.type == deepankumarpn.github.io.expensetracker.domain.model.TransactionType.INCOME }
                        .sumOf { it.amount }
                    val expense = transactions.filter { it.type == deepankumarpn.github.io.expensetracker.domain.model.TransactionType.EXPENSE }
                        .sumOf { it.amount }

                    setState {
                        copy(
                            isLoading = false,
                            transactions = transactions.sortedByDescending { it.createdAt },
                            totalIncome = income,
                            totalExpense = expense,
                            currency = settings.currency
                        )
                    }
                }
        }
    }
}

package deepankumarpn.github.io.expensetracker.presentation.ui.screens.home

import androidx.lifecycle.viewModelScope
import deepankumarpn.github.io.expensetracker.base.BaseViewModel
import deepankumarpn.github.io.expensetracker.base.StateFullResult
import deepankumarpn.github.io.expensetracker.domain.model.Category
import deepankumarpn.github.io.expensetracker.domain.model.DurationFilter
import deepankumarpn.github.io.expensetracker.domain.model.PaymentType
import deepankumarpn.github.io.expensetracker.domain.model.Transaction
import deepankumarpn.github.io.expensetracker.domain.repository.CategoryRepository
import deepankumarpn.github.io.expensetracker.domain.repository.PaymentTypeRepository
import deepankumarpn.github.io.expensetracker.domain.repository.SettingsRepository
import deepankumarpn.github.io.expensetracker.domain.repository.TransactionRepository
import deepankumarpn.github.io.expensetracker.utils.generateUUID
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * ViewModel for Home screen.
 */
class HomeViewModel(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val paymentTypeRepository: PaymentTypeRepository,
    private val settingsRepository: SettingsRepository
) : BaseViewModel<HomeContract.State, HomeContract.Event, HomeContract.Effect>(
    initialState = HomeContract.State(
        selectedDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    )
) {

    init {
        loadInitialData()
    }

    override fun onEvent(event: HomeContract.Event) {
        when (event) {
            is HomeContract.Event.TypeSelected -> {
                setState { copy(transactionType = event.type) }
            }
            is HomeContract.Event.AmountChanged -> {
                setState { copy(amount = event.amount) }
            }
            is HomeContract.Event.NoteChanged -> {
                setState { copy(note = event.note) }
            }
            is HomeContract.Event.CategorySelected -> {
                setState { copy(selectedCategory = event.category) }
            }
            is HomeContract.Event.PaymentTypeSelected -> {
                setState { copy(selectedPaymentType = event.paymentType) }
            }
            is HomeContract.Event.DateSelected -> {
                setState { copy(selectedDate = event.date, showDatePicker = false) }
            }
            HomeContract.Event.ShowDatePicker -> {
                setState { copy(showDatePicker = true) }
            }
            HomeContract.Event.DismissDatePicker -> {
                setState { copy(showDatePicker = false) }
            }
            HomeContract.Event.SubmitTransaction -> submitTransaction()
            HomeContract.Event.LoadSummary -> loadSummary()
        }
    }

    private fun loadInitialData() {
        loadCategories()
        loadPaymentTypes()
        loadSummary()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoryRepository.getCategories()
                .catch { e ->
                    setState { copy(error = e.message) }
                }
                .collect { categories ->
                    setState {
                        copy(
                            categories = categories,
                            selectedCategory = selectedCategory ?: categories.firstOrNull()
                        )
                    }
                }
        }
    }

    private fun loadPaymentTypes() {
        viewModelScope.launch {
            paymentTypeRepository.getPaymentTypes()
                .catch { e ->
                    setState { copy(error = e.message) }
                }
                .collect { paymentTypes ->
                    setState {
                        copy(
                            paymentTypes = paymentTypes,
                            selectedPaymentType = selectedPaymentType ?: paymentTypes.firstOrNull()
                        )
                    }
                }
        }
    }

    private fun loadSummary() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }

            settingsRepository.getUserSettings().collect { settings ->
                val filter = settings.homeDuration

                transactionRepository.getTransactionsByFilter(filter)
                    .catch { e ->
                        setState { copy(isLoading = false, error = e.message) }
                    }
                    .collect { transactions ->
                        val income = transactions.filter { it.type == deepankumarpn.github.io.expensetracker.domain.model.TransactionType.INCOME }
                            .sumOf { it.amount }
                        val expense = transactions.filter { it.type == deepankumarpn.github.io.expensetracker.domain.model.TransactionType.EXPENSE }
                            .sumOf { it.amount }

                        setState {
                            copy(
                                isLoading = false,
                                totalIncome = income,
                                totalExpense = expense,
                                currency = settings.currency,
                                durationLabel = when (filter) {
                                    DurationFilter.DAILY -> "Today"
                                    DurationFilter.WEEKLY -> "This Week"
                                    DurationFilter.MONTHLY -> "This Month"
                                    DurationFilter.QUARTER_YEAR -> "Last 3 Months"
                                    DurationFilter.HALF_YEAR -> "Last 6 Months"
                                    DurationFilter.CUSTOM -> "Last ${settings.customDays} Days"
                                }
                            )
                        }
                    }
            }
        }
    }

    private fun submitTransaction() {
        val state = currentState

        // Validation
        if (state.amount.isBlank() || state.amount.toDoubleOrNull() == null) {
            setEffect(HomeContract.Effect.ShowError("Please enter a valid amount"))
            return
        }

        if (state.selectedCategory == null) {
            setEffect(HomeContract.Effect.ShowError("Please select a category"))
            return
        }

        if (state.selectedPaymentType == null) {
            setEffect(HomeContract.Effect.ShowError("Please select a payment type"))
            return
        }

        val selectedDate = state.selectedDate ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

        viewModelScope.launch {
            setState { copy(isSubmitting = true) }

            val transaction = Transaction(
                id = generateUUID(),
                type = state.transactionType,
                amount = state.amount.toDouble(),
                category = state.selectedCategory.name,
                paymentType = state.selectedPaymentType.name,
                note = state.note,
                date = selectedDate,
                createdAt = Clock.System.now()
            )

            when (val result = transactionRepository.addTransaction(transaction)) {
                is StateFullResult.Success -> {
                    setState {
                        copy(
                            isSubmitting = false,
                            amount = "",
                            note = "",
                            selectedDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                        )
                    }
                    setEffect(HomeContract.Effect.TransactionAdded)
                    setEffect(HomeContract.Effect.ShowSuccess("Transaction added successfully"))
                }
                is StateFullResult.Error -> {
                    setState { copy(isSubmitting = false) }
                    setEffect(HomeContract.Effect.ShowError(result.message))
                }
                is StateFullResult.Loading -> {
                    setState { copy(isSubmitting = true) }
                }
            }
        }
    }
}

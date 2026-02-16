package deepankumarpn.github.io.expensetracker.presentation.ui.screens.settings

import androidx.lifecycle.viewModelScope
import deepankumarpn.github.io.expensetracker.base.BaseViewModel
import deepankumarpn.github.io.expensetracker.base.StateFullResult
import deepankumarpn.github.io.expensetracker.domain.repository.AuthRepository
import deepankumarpn.github.io.expensetracker.domain.repository.CategoryRepository
import deepankumarpn.github.io.expensetracker.domain.repository.PaymentTypeRepository
import deepankumarpn.github.io.expensetracker.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/**
 * ViewModel for Settings screen.
 */
class SettingsViewModel(
    private val authRepository: AuthRepository,
    private val settingsRepository: SettingsRepository,
    private val categoryRepository: CategoryRepository,
    private val paymentTypeRepository: PaymentTypeRepository
) : BaseViewModel<SettingsContract.State, SettingsContract.Event, SettingsContract.Effect>(
    initialState = SettingsContract.State()
) {

    init {
        loadProfile()
        loadSettings()
        loadCategories()
        loadPaymentTypes()
    }

    override fun onEvent(event: SettingsContract.Event) {
        when (event) {
            SettingsContract.Event.LoadProfile -> loadProfile()
            is SettingsContract.Event.CurrencyChanged -> updateCurrency(event.currency)
            SettingsContract.Event.LoadCategories -> loadCategories()
            is SettingsContract.Event.AddCategory -> addCategory(event.name)
            is SettingsContract.Event.UpdateCategory -> updateCategory(event.id, event.name)
            is SettingsContract.Event.DeleteCategory -> deleteCategory(event.id)
            SettingsContract.Event.LoadPaymentTypes -> loadPaymentTypes()
            is SettingsContract.Event.AddPaymentType -> addPaymentType(event.name)
            is SettingsContract.Event.UpdatePaymentType -> updatePaymentType(event.id, event.name)
            is SettingsContract.Event.DeletePaymentType -> deletePaymentType(event.id)
            is SettingsContract.Event.HomeDurationChanged -> updateHomeDuration(event.duration)
            is SettingsContract.Event.CustomDaysChanged -> updateCustomDays(event.days)
            SettingsContract.Event.SignOut -> signOut()
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            authRepository.getCurrentUser().collect { profile ->
                setState {
                    copy(
                        userName = profile?.name ?: "",
                        userEmail = profile?.email ?: ""
                    )
                }
            }
        }
    }

    private fun loadSettings() {
        viewModelScope.launch {
            settingsRepository.getUserSettings().collect { settings ->
                setState {
                    copy(
                        selectedCurrency = settings.currency,
                        homeDuration = settings.homeDuration,
                        customDays = settings.customDays
                    )
                }
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoryRepository.getCategories()
                .catch { e ->
                    setState { copy(error = e.message) }
                }
                .collect { categories ->
                    setState { copy(categories = categories) }
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
                    setState { copy(paymentTypes = paymentTypes) }
                }
        }
    }

    private fun updateCurrency(currency: deepankumarpn.github.io.expensetracker.domain.model.CurrencyType) {
        viewModelScope.launch {
            when (settingsRepository.updateCurrency(currency)) {
                is StateFullResult.Success -> {
                    setState { copy(selectedCurrency = currency) }
                    setEffect(SettingsContract.Effect.ShowSuccess("Currency updated"))
                }
                is StateFullResult.Error -> {
                    setEffect(SettingsContract.Effect.ShowError("Failed to update currency"))
                }
                else -> {}
            }
        }
    }

    private fun addCategory(name: String) {
        viewModelScope.launch {
            when (categoryRepository.addCategory(name)) {
                is StateFullResult.Success -> {
                    setEffect(SettingsContract.Effect.ShowSuccess("Category added"))
                }
                is StateFullResult.Error -> {
                    setEffect(SettingsContract.Effect.ShowError("Failed to add category"))
                }
                else -> {}
            }
        }
    }

    private fun updateCategory(id: String, name: String) {
        viewModelScope.launch {
            when (categoryRepository.updateCategory(id, name)) {
                is StateFullResult.Success -> {
                    setEffect(SettingsContract.Effect.ShowSuccess("Category updated"))
                }
                is StateFullResult.Error -> {
                    setEffect(SettingsContract.Effect.ShowError("Failed to update category"))
                }
                else -> {}
            }
        }
    }

    private fun deleteCategory(id: String) {
        viewModelScope.launch {
            val category = currentState.categories.find { it.id == id }
            if (category != null) {
                val isInUse = categoryRepository.isCategoryInUse(category.name)
                if (isInUse) {
                    setEffect(SettingsContract.Effect.ShowDeleteBlockedMessage(category.name))
                    return@launch
                }
            }

            when (categoryRepository.deleteCategory(id)) {
                is StateFullResult.Success -> {
                    setEffect(SettingsContract.Effect.ShowSuccess("Category deleted"))
                }
                is StateFullResult.Error -> {
                    setEffect(SettingsContract.Effect.ShowError("Failed to delete category"))
                }
                else -> {}
            }
        }
    }

    private fun addPaymentType(name: String) {
        viewModelScope.launch {
            when (paymentTypeRepository.addPaymentType(name)) {
                is StateFullResult.Success -> {
                    setEffect(SettingsContract.Effect.ShowSuccess("Payment type added"))
                }
                is StateFullResult.Error -> {
                    setEffect(SettingsContract.Effect.ShowError("Failed to add payment type"))
                }
                else -> {}
            }
        }
    }

    private fun updatePaymentType(id: String, name: String) {
        viewModelScope.launch {
            when (paymentTypeRepository.updatePaymentType(id, name)) {
                is StateFullResult.Success -> {
                    setEffect(SettingsContract.Effect.ShowSuccess("Payment type updated"))
                }
                is StateFullResult.Error -> {
                    setEffect(SettingsContract.Effect.ShowError("Failed to update payment type"))
                }
                else -> {}
            }
        }
    }

    private fun deletePaymentType(id: String) {
        viewModelScope.launch {
            val paymentType = currentState.paymentTypes.find { it.id == id }
            if (paymentType != null) {
                val isInUse = paymentTypeRepository.isPaymentTypeInUse(paymentType.name)
                if (isInUse) {
                    setEffect(SettingsContract.Effect.ShowDeleteBlockedMessage(paymentType.name))
                    return@launch
                }
            }

            when (paymentTypeRepository.deletePaymentType(id)) {
                is StateFullResult.Success -> {
                    setEffect(SettingsContract.Effect.ShowSuccess("Payment type deleted"))
                }
                is StateFullResult.Error -> {
                    setEffect(SettingsContract.Effect.ShowError("Failed to delete payment type"))
                }
                else -> {}
            }
        }
    }

    private fun updateHomeDuration(duration: deepankumarpn.github.io.expensetracker.domain.model.DurationFilter) {
        viewModelScope.launch {
            when (settingsRepository.updateHomeDuration(duration)) {
                is StateFullResult.Success -> {
                    setState { copy(homeDuration = duration) }
                    setEffect(SettingsContract.Effect.ShowSuccess("Duration updated"))
                }
                is StateFullResult.Error -> {
                    setEffect(SettingsContract.Effect.ShowError("Failed to update duration"))
                }
                else -> {}
            }
        }
    }

    private fun updateCustomDays(days: Int) {
        viewModelScope.launch {
            when (settingsRepository.updateCustomDays(days)) {
                is StateFullResult.Success -> {
                    setState { copy(customDays = days) }
                    setEffect(SettingsContract.Effect.ShowSuccess("Custom days updated"))
                }
                is StateFullResult.Error -> {
                    setEffect(SettingsContract.Effect.ShowError("Failed to update custom days"))
                }
                else -> {}
            }
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            when (authRepository.signOut()) {
                is StateFullResult.Success -> {
                    setEffect(SettingsContract.Effect.NavigateToLogin)
                }
                is StateFullResult.Error -> {
                    setEffect(SettingsContract.Effect.ShowError("Failed to sign out"))
                }
                else -> {}
            }
        }
    }
}

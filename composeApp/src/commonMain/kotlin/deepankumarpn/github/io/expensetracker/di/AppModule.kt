package deepankumarpn.github.io.expensetracker.di

import deepankumarpn.github.io.expensetracker.data.repository.AuthRepositoryImpl
import deepankumarpn.github.io.expensetracker.data.repository.CategoryRepositoryImpl
import deepankumarpn.github.io.expensetracker.data.repository.PaymentTypeRepositoryImpl
import deepankumarpn.github.io.expensetracker.data.repository.SettingsRepositoryImpl
import deepankumarpn.github.io.expensetracker.data.repository.TransactionRepositoryImpl
import deepankumarpn.github.io.expensetracker.domain.repository.AuthRepository
import deepankumarpn.github.io.expensetracker.domain.repository.CategoryRepository
import deepankumarpn.github.io.expensetracker.domain.repository.PaymentTypeRepository
import deepankumarpn.github.io.expensetracker.domain.repository.SettingsRepository
import deepankumarpn.github.io.expensetracker.domain.repository.TransactionRepository
import deepankumarpn.github.io.expensetracker.presentation.ui.screens.home.HomeViewModel
import deepankumarpn.github.io.expensetracker.presentation.ui.screens.history.HistoryViewModel
import deepankumarpn.github.io.expensetracker.presentation.ui.screens.login.LoginViewModel
import deepankumarpn.github.io.expensetracker.presentation.ui.screens.settings.SettingsViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Koin dependency injection module for the app.
 */
val appModule = module {
    // Repositories
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class
    singleOf(::TransactionRepositoryImpl) bind TransactionRepository::class
    singleOf(::CategoryRepositoryImpl) bind CategoryRepository::class
    singleOf(::PaymentTypeRepositoryImpl) bind PaymentTypeRepository::class
    singleOf(::SettingsRepositoryImpl) bind SettingsRepository::class

    // ViewModels
    viewModelOf(::LoginViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::HistoryViewModel)
    viewModelOf(::SettingsViewModel)
}

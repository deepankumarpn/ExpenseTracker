package deepankumarpn.github.io.expensetracker.data.repository

import deepankumarpn.github.io.expensetracker.base.StateFullResult
import deepankumarpn.github.io.expensetracker.domain.model.DurationFilter
import deepankumarpn.github.io.expensetracker.domain.model.Transaction
import deepankumarpn.github.io.expensetracker.domain.model.TransactionType
import deepankumarpn.github.io.expensetracker.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

/**
 * In-memory implementation of TransactionRepository.
 */
class TransactionRepositoryImpl : TransactionRepository {
    private val transactions = MutableStateFlow<List<Transaction>>(emptyList())

    override suspend fun addTransaction(transaction: Transaction): StateFullResult<Unit> {
        return try {
            transactions.value = transactions.value + transaction
            StateFullResult.Success(Unit)
        } catch (e: Exception) {
            StateFullResult.Error(e.message ?: "Failed to add transaction")
        }
    }

    override fun getTransactions(): Flow<List<Transaction>> = transactions

    override fun getTransactionsByDateRange(
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<Transaction>> {
        return transactions.map { list ->
            list.filter { it.date in startDate..endDate }
        }
    }

    override fun getTransactionsByFilter(
        filter: DurationFilter,
        year: Int?,
        month: Int?,
        customDays: Int?
    ): Flow<List<Transaction>> {
        return transactions.map { list ->
            val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            val today = now.date

            when (filter) {
                DurationFilter.DAILY -> list.filter { it.date == today }
                DurationFilter.WEEKLY -> {
                    val weekAgo = today.minus(DatePeriod(days = 7))
                    list.filter { it.date >= weekAgo }
                }
                DurationFilter.MONTHLY -> {
                    list.filter { it.date.year == now.year && it.date.monthNumber == now.monthNumber }
                }
                DurationFilter.QUARTER_YEAR -> {
                    val quarterAgo = today.minus(DatePeriod(days = 90))
                    list.filter { it.date >= quarterAgo }
                }
                DurationFilter.HALF_YEAR -> {
                    val halfYearAgo = today.minus(DatePeriod(days = 180))
                    list.filter { it.date >= halfYearAgo }
                }
                DurationFilter.CUSTOM -> {
                    val daysAgo = today.minus(DatePeriod(days = customDays ?: 30))
                    list.filter { it.date >= daysAgo }
                }
            }
        }
    }

    override suspend fun getTotalIncome(startDate: LocalDate, endDate: LocalDate): Double {
        return transactions.value
            .filter { it.type == TransactionType.INCOME && it.date in startDate..endDate }
            .sumOf { it.amount }
    }

    override suspend fun getTotalExpense(startDate: LocalDate, endDate: LocalDate): Double {
        return transactions.value
            .filter { it.type == TransactionType.EXPENSE && it.date in startDate..endDate }
            .sumOf { it.amount }
    }

    override fun getTransactionsByType(type: TransactionType): Flow<List<Transaction>> {
        return transactions.map { list -> list.filter { it.type == type } }
    }

    override fun getTransactionsByPaymentType(paymentType: String): Flow<List<Transaction>> {
        return transactions.map { list -> list.filter { it.paymentType == paymentType } }
    }

    override fun getTransactionsByCategory(category: String): Flow<List<Transaction>> {
        return transactions.map { list -> list.filter { it.category == category } }
    }

    override suspend fun updateTransaction(transaction: Transaction): StateFullResult<Unit> {
        return try {
            transactions.value = transactions.value.map {
                if (it.id == transaction.id) transaction else it
            }
            StateFullResult.Success(Unit)
        } catch (e: Exception) {
            StateFullResult.Error(e.message ?: "Failed to update transaction")
        }
    }

    override suspend fun deleteTransaction(transactionId: String): StateFullResult<Unit> {
        return try {
            transactions.value = transactions.value.filter { it.id != transactionId }
            StateFullResult.Success(Unit)
        } catch (e: Exception) {
            StateFullResult.Error(e.message ?: "Failed to delete transaction")
        }
    }
}

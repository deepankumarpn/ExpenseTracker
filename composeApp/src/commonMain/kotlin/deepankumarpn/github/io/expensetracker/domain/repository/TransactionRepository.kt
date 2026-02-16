package deepankumarpn.github.io.expensetracker.domain.repository

import deepankumarpn.github.io.expensetracker.base.StateFullResult
import deepankumarpn.github.io.expensetracker.domain.model.DurationFilter
import deepankumarpn.github.io.expensetracker.domain.model.Transaction
import deepankumarpn.github.io.expensetracker.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

/**
 * Repository interface for transaction operations.
 */
interface TransactionRepository {
    /**
     * Add a new transaction.
     */
    suspend fun addTransaction(transaction: Transaction): StateFullResult<Unit>

    /**
     * Get all transactions.
     */
    fun getTransactions(): Flow<List<Transaction>>

    /**
     * Get transactions filtered by date range.
     */
    fun getTransactionsByDateRange(
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<Transaction>>

    /**
     * Get transactions filtered by duration filter.
     */
    fun getTransactionsByFilter(
        filter: DurationFilter,
        year: Int? = null,
        month: Int? = null,
        customDays: Int? = null
    ): Flow<List<Transaction>>

    /**
     * Get total income for a date range.
     */
    suspend fun getTotalIncome(startDate: LocalDate, endDate: LocalDate): Double

    /**
     * Get total expense for a date range.
     */
    suspend fun getTotalExpense(startDate: LocalDate, endDate: LocalDate): Double

    /**
     * Get transactions by type.
     */
    fun getTransactionsByType(type: TransactionType): Flow<List<Transaction>>

    /**
     * Get transactions by payment type.
     */
    fun getTransactionsByPaymentType(paymentType: String): Flow<List<Transaction>>

    /**
     * Get transactions by category.
     */
    fun getTransactionsByCategory(category: String): Flow<List<Transaction>>

    /**
     * Update a transaction.
     */
    suspend fun updateTransaction(transaction: Transaction): StateFullResult<Unit>

    /**
     * Delete a transaction.
     */
    suspend fun deleteTransaction(transactionId: String): StateFullResult<Unit>
}

package deepankumarpn.github.io.expensetracker.domain.repository

import deepankumarpn.github.io.expensetracker.base.StateFullResult
import deepankumarpn.github.io.expensetracker.domain.model.Category
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for category operations.
 */
interface CategoryRepository {
    /**
     * Get all categories.
     */
    fun getCategories(): Flow<List<Category>>

    /**
     * Add a new category.
     */
    suspend fun addCategory(name: String): StateFullResult<Category>

    /**
     * Update an existing category.
     */
    suspend fun updateCategory(id: String, name: String): StateFullResult<Unit>

    /**
     * Delete a category.
     */
    suspend fun deleteCategory(id: String): StateFullResult<Unit>

    /**
     * Check if a category is being used in any transactions.
     */
    suspend fun isCategoryInUse(categoryName: String): Boolean
}

package deepankumarpn.github.io.expensetracker.data.repository

import deepankumarpn.github.io.expensetracker.base.StateFullResult
import deepankumarpn.github.io.expensetracker.domain.model.Category
import deepankumarpn.github.io.expensetracker.domain.repository.CategoryRepository
import deepankumarpn.github.io.expensetracker.utils.generateUUID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.datetime.Clock

/**
 * In-memory implementation of CategoryRepository.
 */
class CategoryRepositoryImpl : CategoryRepository {
    private val categories = MutableStateFlow(
        listOf(
            Category("1", "Food", true, Clock.System.now()),
            Category("2", "Travel", true, Clock.System.now()),
            Category("3", "Shopping", false, Clock.System.now()),
            Category("4", "Bills", false, Clock.System.now()),
            Category("5", "Entertainment", false, Clock.System.now())
        )
    )

    override fun getCategories(): Flow<List<Category>> = categories

    override suspend fun addCategory(name: String): StateFullResult<Category> {
        return try {
            val newCategory = Category(
                id = generateUUID(),
                name = name,
                isDefault = false,
                createdAt = Clock.System.now()
            )
            categories.value = categories.value + newCategory
            StateFullResult.Success(newCategory)
        } catch (e: Exception) {
            StateFullResult.Error(e.message ?: "Failed to add category")
        }
    }

    override suspend fun updateCategory(id: String, name: String): StateFullResult<Unit> {
        return try {
            categories.value = categories.value.map {
                if (it.id == id) it.copy(name = name) else it
            }
            StateFullResult.Success(Unit)
        } catch (e: Exception) {
            StateFullResult.Error(e.message ?: "Failed to update category")
        }
    }

    override suspend fun deleteCategory(id: String): StateFullResult<Unit> {
        return try {
            categories.value = categories.value.filter { it.id != id }
            StateFullResult.Success(Unit)
        } catch (e: Exception) {
            StateFullResult.Error(e.message ?: "Failed to delete category")
        }
    }

    override suspend fun isCategoryInUse(categoryName: String): Boolean {
        // In a real implementation, this would check the transaction repository
        return false
    }
}

package com.project.budgettracker.data.repositories

import com.project.budgettracker.data.dao.CategoryDao
import com.project.budgettracker.data.entities.Category
import kotlinx.coroutines.flow.Flow

class CategoryRepositoryImpl (
    private val categoryDao: CategoryDao
) : CategoryRepository {

    override suspend fun insertCategory(category: Category) {
        categoryDao.insert(category)
    }

    override suspend fun updateCategory(category: Category) {
        categoryDao.update(category)
    }

    override suspend fun deleteCategory(category: Category) {
        categoryDao.delete(category)
    }

    override fun getAllCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories()
    }

    override fun getCategoryById(id: Int): Flow<Category?> {
        return categoryDao.getCategoryById(id)
    }
}

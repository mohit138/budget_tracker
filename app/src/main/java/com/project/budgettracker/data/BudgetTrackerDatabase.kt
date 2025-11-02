package com.project.budgettracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.project.budgettracker.data.dao.CategoryDao
import com.project.budgettracker.data.dao.ExpenseDao
import com.project.budgettracker.data.entities.Category
import com.project.budgettracker.data.entities.Expense

@Database(
    entities = [Category::class, Expense::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class BudgetTrackerDatabase: RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun expenseDao(): ExpenseDao

    companion object{
        const val DATABASE_NAME = "budget_tracker_database"
        @Volatile
        private var INSTANCE: BudgetTrackerDatabase? = null

        fun getDatabase(context: Context): BudgetTrackerDatabase {
            return INSTANCE ?: synchronized(this) {

                Room.databaseBuilder(
                    context.applicationContext,
                    BudgetTrackerDatabase::class.java,
                    DATABASE_NAME)
                    .build()
                    .also {
                        INSTANCE = it
                    }
            }
        }
    }
}
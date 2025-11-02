package com.project.budgettracker

import android.app.Application
import com.project.budgettracker.data.AppContainer
import com.project.budgettracker.data.AppDataContainer

class BudgetTrackerApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}

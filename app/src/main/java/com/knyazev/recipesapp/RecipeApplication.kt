package com.knyazev.recipesapp

import android.app.Application
import com.knyazev.recipesapp.di.AppContainer

class RecipeApplication : Application() {

    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}
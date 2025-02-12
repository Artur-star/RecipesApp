package com.knyazev.recipesapp.di

interface Factory<T> {
    fun create(): T
}
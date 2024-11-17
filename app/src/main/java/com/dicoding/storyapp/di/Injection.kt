package com.dicoding.storyapp.di

import android.content.Context
import com.dicoding.storyapp.data.api.ApiConfig
import com.dicoding.storyapp.data.repository.UserRepository
import com.dicoding.storyapp.data.pref.UserPreference
import com.dicoding.storyapp.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(apiService, pref)
    }

    fun provideApiService() = ApiConfig.getApiService()
}
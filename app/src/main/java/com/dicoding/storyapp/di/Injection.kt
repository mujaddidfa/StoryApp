package com.dicoding.storyapp.di

import android.content.Context
import com.dicoding.storyapp.data.api.ApiService
import com.dicoding.storyapp.data.repository.UserRepository
import com.dicoding.storyapp.data.pref.UserPreference
import com.dicoding.storyapp.data.pref.dataStore
import com.dicoding.storyapp.data.repository.RegisterRepository

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }

    fun provideRegisterRepository(apiService: ApiService): RegisterRepository {
        return RegisterRepository(apiService)
    }
}
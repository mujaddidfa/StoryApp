package com.dicoding.storyapp.di

import android.content.Context
import com.dicoding.storyapp.data.api.ApiConfig
import com.dicoding.storyapp.data.database.StoryDatabase
import com.dicoding.storyapp.data.repository.StoryRepository
import com.dicoding.storyapp.data.pref.UserPreference
import com.dicoding.storyapp.data.pref.dataStore
import com.dicoding.storyapp.data.repository.AuthRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    private var storyRepository: StoryRepository? = null

    fun provideAuthRepository(context: Context): AuthRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return AuthRepository.getInstance(apiService, pref)
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        if (storyRepository == null) {
            val pref = UserPreference.getInstance(context.dataStore)
            val user = runBlocking { pref.getUser().first() }
            val apiService = ApiConfig.getApiService()
            val storyDatabase = StoryDatabase.getDatabase(context)
            storyRepository = StoryRepository.getInstance(storyDatabase, apiService)
            user.token.let { token ->
                ApiConfig.setToken(token)
                storyRepository?.updateToken(token)
            }
        }
        return storyRepository!!
    }

    fun updateStoryRepositoryToken(token: String) {
        storyRepository?.updateToken(token)
    }
}
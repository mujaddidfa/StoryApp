package com.dicoding.storyapp.di

import android.content.Context
import com.dicoding.storyapp.data.repository.StoryRepository
import com.dicoding.storyapp.data.pref.UserPreference
import com.dicoding.storyapp.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return StoryRepository.getInstance(pref)
    }
}
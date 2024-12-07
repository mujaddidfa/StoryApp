package com.dicoding.storyapp.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.data.repository.AuthRepository
import com.dicoding.storyapp.data.repository.StoryRepository
import com.dicoding.storyapp.di.Injection
import com.dicoding.storyapp.view.addstory.AddStoryViewModel
import com.dicoding.storyapp.view.login.LoginViewModel
import com.dicoding.storyapp.view.main.MainViewModel
import com.dicoding.storyapp.view.maps.MapsViewModel
import com.dicoding.storyapp.view.signup.SignupViewModel

class ViewModelFactory(
    private val authRepository: AuthRepository,
    private val storyRepository: StoryRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(authRepository, storyRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(authRepository) as T
            }
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(authRepository) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(storyRepository) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(storyRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                val authRepository = Injection.provideAuthRepository(context)
                val storyRepository = Injection.provideStoryRepository(context)
                INSTANCE ?: ViewModelFactory(authRepository, storyRepository)
            }.also { INSTANCE = it }
        }
    }
}
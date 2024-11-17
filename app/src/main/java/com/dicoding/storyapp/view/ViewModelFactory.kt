package com.dicoding.storyapp.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.data.api.ApiService
import com.dicoding.storyapp.data.repository.UserRepository
import com.dicoding.storyapp.di.Injection
import com.dicoding.storyapp.view.login.LoginViewModel
import com.dicoding.storyapp.view.main.MainViewModel
import com.dicoding.storyapp.view.signup.SignupViewModel

class ViewModelFactory(
    private val apiService: ApiService,
    private val repository: UserRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(apiService) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    val apiService = Injection.provideApiService()
                    val repository = Injection.provideRepository(context)
                    INSTANCE = ViewModelFactory(apiService, repository)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}
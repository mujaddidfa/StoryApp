package com.dicoding.storyapp.view.signup

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.data.api.ApiService
import com.dicoding.storyapp.di.Injection

class SignupViewModelFactory(private val context: Context, private val apiService: ApiService) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                val registerRepository = Injection.provideRegisterRepository(apiService)
                SignupViewModel(registerRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: SignupViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context, apiService: ApiService): SignupViewModelFactory {
            if (INSTANCE == null) {
                synchronized(SignupViewModelFactory::class.java) {
                    INSTANCE = SignupViewModelFactory(context, apiService)
                }
            }
            return INSTANCE as SignupViewModelFactory
        }
    }
}
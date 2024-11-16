package com.dicoding.storyapp.view.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.dicoding.storyapp.data.repository.RegisterRepository
import com.dicoding.storyapp.data.Result
import kotlinx.coroutines.Dispatchers

class SignupViewModel(private val registerRepository: RegisterRepository) : ViewModel() {
    fun register(name: String, email: String, password: String) = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        val result = registerRepository.register(name, email, password)
        emit(result)
    }
}
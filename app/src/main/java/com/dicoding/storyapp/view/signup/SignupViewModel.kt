package com.dicoding.storyapp.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.dicoding.storyapp.utils.Result
import com.dicoding.storyapp.data.api.response.ErrorResponse
import com.dicoding.storyapp.data.repository.AuthRepository
import com.google.gson.Gson
import retrofit2.HttpException

class SignupViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun register(name: String, email: String, password: String) = liveData {
        _isLoading.value = true
        try {
            val response = repository.register(name, email, password)
            emit(Result.Success(response.message!!))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            emit(errorBody.message?.let { Result.Error(it) })
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error"))
        } finally {
            _isLoading.value = false
        }
    }
}
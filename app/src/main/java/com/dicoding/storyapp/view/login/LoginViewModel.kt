package com.dicoding.storyapp.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.api.ApiConfig
import com.dicoding.storyapp.data.api.response.ErrorResponse
import com.dicoding.storyapp.data.repository.AuthRepository
import com.dicoding.storyapp.data.pref.UserModel
import com.dicoding.storyapp.di.Injection
import com.dicoding.storyapp.utils.EspressoIdlingResource
import com.dicoding.storyapp.utils.Result
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun login(email: String, password: String) = liveData {
        EspressoIdlingResource.increment()
        _isLoading.value = true
        try {
            val response = repository.login(email, password)
            response.loginResult?.token?.let { token ->
                repository.saveToken(token)
                saveUser(UserModel(email, token))
                ApiConfig.setToken(token)
                Injection.updateStoryRepositoryToken(token)
            }
            emit(Result.Success(response.message!!))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            emit(errorBody.message?.let { Result.Error(it) })
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error"))
        } finally {
            _isLoading.value = false
            EspressoIdlingResource.decrement()
        }
    }

    private fun saveUser(user: UserModel) {
        viewModelScope.launch {
            repository.saveUser(user)
        }
    }
}
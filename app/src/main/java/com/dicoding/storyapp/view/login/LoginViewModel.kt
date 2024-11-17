package com.dicoding.storyapp.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.api.ErrorResponse
import com.dicoding.storyapp.data.repository.UserRepository
import com.dicoding.storyapp.data.pref.UserModel
import com.dicoding.storyapp.utils.Result
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    fun login(email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val response = repository.login(email, password)
            response.loginResult?.token?.let { token ->
                repository.saveToken(token)
                saveSession(UserModel(email, token))
            }
            emit(Result.Success(response.message!!))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            emit(errorBody.message?.let { Result.Error(it) })
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error"))
        }
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}
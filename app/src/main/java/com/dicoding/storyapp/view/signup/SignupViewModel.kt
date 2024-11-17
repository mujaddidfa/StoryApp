package com.dicoding.storyapp.view.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.dicoding.storyapp.utils.Result
import com.dicoding.storyapp.data.api.ApiService
import com.dicoding.storyapp.data.api.ErrorResponse
import com.google.gson.Gson
import retrofit2.HttpException

class SignupViewModel(private val apiService: ApiService) : ViewModel() {
    fun register(name: String, email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(name, email, password)
            emit(Result.Success(response.message!!))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            emit(errorBody.message?.let { Result.Error(it) })
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error"))
        }
    }
}
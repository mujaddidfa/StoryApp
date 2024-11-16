package com.dicoding.storyapp.data.repository

import com.dicoding.storyapp.data.api.ApiService
import com.dicoding.storyapp.data.api.RegisterResponse
import com.dicoding.storyapp.data.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RegisterRepository(private val apiService: ApiService) {
    suspend fun register(name: String, email: String, password: String): Result<RegisterResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.register(name, email, password)
                if (response.error == false) {
                    Result.Success(response)
                } else {
                    Result.Error(response.message ?: "Unknown error")
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "Network error")
            }
        }
    }
}
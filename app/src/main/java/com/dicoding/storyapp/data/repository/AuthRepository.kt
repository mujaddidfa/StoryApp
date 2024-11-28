package com.dicoding.storyapp.data.repository

import com.dicoding.storyapp.data.api.ApiService
import com.dicoding.storyapp.data.pref.UserModel
import com.dicoding.storyapp.data.pref.UserPreference

class AuthRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {
    suspend fun login(email: String, password: String) = apiService.login(email, password)

    suspend fun register(name: String, email: String, password: String) = apiService.register(name, email, password)

    suspend fun saveUser(user: UserModel) {
        userPreference.saveUser(user)
    }

    suspend fun saveToken(token: String) {
        userPreference.saveToken(token)
    }

    fun getUser() = userPreference.getUser()

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: AuthRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(apiService, userPreference)
            }.also { instance = it }
    }
}
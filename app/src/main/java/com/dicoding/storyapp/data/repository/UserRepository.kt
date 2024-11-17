package com.dicoding.storyapp.data.repository

import com.dicoding.storyapp.data.api.ApiService
import com.dicoding.storyapp.data.pref.UserModel
import com.dicoding.storyapp.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
){
    suspend fun login(email: String, password: String) = apiService.login(email, password)

    suspend fun saveSession(user: UserModel){
        userPreference.saveSession(user)
    }

    suspend fun saveToken(token: String){
        userPreference.saveToken(token)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPreference)
            }.also { instance = it }
    }
}
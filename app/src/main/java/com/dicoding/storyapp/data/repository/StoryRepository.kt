package com.dicoding.storyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.storyapp.data.StoryRemoteMediator
import com.dicoding.storyapp.data.api.ApiService
import com.dicoding.storyapp.data.api.response.FileUploadResponse
import com.dicoding.storyapp.data.api.response.ListStoryItem
import com.dicoding.storyapp.data.database.StoryDatabase
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository private constructor(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService
) {
    private var token: String? = null

    fun updateToken(newToken: String) {
        token = newToken
    }

    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 20
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStories()
            }
        ).liveData
    }

    suspend fun getStoriesWithLocation(location: Int = 1): List<ListStoryItem> {
        val response = apiService.getStoriesWithLocation(location)
        return response.listStory
    }

    suspend fun uploadStory(description: RequestBody, photo: MultipartBody.Part): FileUploadResponse {
        return apiService.uploadStory(description, photo)
    }

    suspend fun uploadStory(description: RequestBody, photo: MultipartBody.Part, lat: Float? = null, lon: Float? = null): FileUploadResponse {
        return apiService.uploadStory(description, photo, lat, lon)
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            storyDatabase: StoryDatabase,
            apiService: ApiService
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(storyDatabase, apiService)
            }.also { instance = it }
    }
}
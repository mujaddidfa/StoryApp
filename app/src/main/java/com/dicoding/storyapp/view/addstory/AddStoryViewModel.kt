package com.dicoding.storyapp.view.addstory

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.repository.StoryRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val repository: StoryRepository) : ViewModel() {
    fun uploadStory(description: RequestBody, photo: MultipartBody.Part) {
        viewModelScope.launch {
            try {
                repository.uploadStory(description, photo)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
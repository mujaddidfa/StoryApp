package com.dicoding.storyapp.view.addstory

import android.location.Location
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.api.response.FileUploadResponse
import com.dicoding.storyapp.data.repository.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val repository: StoryRepository) : ViewModel() {
    private val _currentImageUri = MutableLiveData<Uri?>()
    val currentImageUri: LiveData<Uri?> get() = _currentImageUri

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _currentLocation = MutableLiveData<Location?>()
    val currentLocation: LiveData<Location?> get() = _currentLocation

    fun setCurrentImageUri(uri: Uri?) {
        _currentImageUri.value = uri
    }

    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    fun setErrorMessage(message: String?) {
        _errorMessage.value = message
    }

    fun setCurrentLocation(location: Location?) {
        _currentLocation.value = location
    }

    suspend fun uploadStory(description: RequestBody, photo: MultipartBody.Part, lat: Float? = null, lon: Float? = null): FileUploadResponse {
        _isLoading.value = true
        return try {
            val response = repository.uploadStory(description, photo, lat, lon)
            _isLoading.value = false
            response
        } catch (e: Exception) {
            _isLoading.value = false
            _errorMessage.value = e.message
            throw e
        }
    }
}
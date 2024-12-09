package com.dicoding.storyapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object NetworkUtils {
    private val networkStatus = MutableLiveData<Boolean>()

    fun getNetworkStatus(context: Context): LiveData<Boolean> {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                networkStatus.postValue(true)
            }

            override fun onLost(network: Network) {
                networkStatus.postValue(false)
            }
        })

        return networkStatus
    }
}
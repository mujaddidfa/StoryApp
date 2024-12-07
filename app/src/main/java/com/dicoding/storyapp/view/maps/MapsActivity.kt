package com.dicoding.storyapp.view.maps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.R

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.dicoding.storyapp.databinding.ActivityMapsBinding
import com.dicoding.storyapp.view.ViewModelFactory
import com.dicoding.storyapp.utils.Result

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var viewModel: MapsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(this)
        )[MapsViewModel::class.java]
        observeStories()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    private fun observeStories() {
        viewModel.getStoriesWithLocation().observe(this) { result ->
            when (result) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    showLoading(false)
                    result.data.forEach { story ->
                        val latLng = LatLng(story.lat ?: 0.0, story.lon ?: 0.0)
                        mMap.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title(story.name)
                                .snippet(story.description)
                        )
                    }
                }
                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    showLoading(false)
                    Toast.makeText(this, "Unknown error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
package com.dicoding.storyapp.view.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.api.ApiConfig
import com.dicoding.storyapp.data.api.response.ListStoryItem
import com.dicoding.storyapp.databinding.ActivityMainBinding
import com.dicoding.storyapp.di.Injection
import com.dicoding.storyapp.utils.NetworkUtils
import com.dicoding.storyapp.view.ViewModelFactory
import com.dicoding.storyapp.view.welcome.WelcomeActivity
import com.dicoding.storyapp.view.addstory.AddStoryActivity
import com.dicoding.storyapp.view.detail.DetailActivity
import com.dicoding.storyapp.view.maps.MapsActivity

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAdapter()
        observeNetworkStatus()
        observeUser()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                viewModel.logout()
                true
            }
            R.id.action_setting -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            R.id.action_maps -> {
                startActivity(Intent(this, MapsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.show()

        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager

        binding.fabAddStory.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }
    }

    private fun setupAdapter() {
        adapter = StoryAdapter()
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter { adapter.retry() }
        )
    }

    private fun observeNetworkStatus() {
        NetworkUtils.getNetworkStatus(this).observe(this) { isConnected ->
            if (isConnected) {
                adapter.retry()
            }
        }
    }

    private fun observeUser() {
        viewModel.getUser().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                ApiConfig.setToken(user.token)
                Injection.updateStoryRepositoryToken(user.token)
                getData()
            }
        }
    }

    private fun getData() {
        viewModel.story.observe(this) {
            adapter.submitData(lifecycle, it)
        }
        adapter.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback {
            override fun onItemClicked(story: ListStoryItem) {
                showDetailStory(story)
            }
        })
    }

    private fun showDetailStory(story: ListStoryItem) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_STORY, story)

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            Pair(binding.rvStory.findViewById(R.id.iv_item_photo), "photo"),
            Pair(binding.rvStory.findViewById(R.id.tv_item_name), "name"),
            Pair(binding.rvStory.findViewById(R.id.tv_item_description), "description")
        )
        startActivity(intent, options.toBundle())
    }
}
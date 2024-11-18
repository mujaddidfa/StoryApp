package com.dicoding.storyapp.view.detail

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.storyapp.data.api.response.ListStoryItem
import com.dicoding.storyapp.databinding.ActivityDetailBinding

@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_STORY, ListStoryItem::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_STORY)
        }

        if (story != null) {
            Glide.with(this)
                .load(story.photoUrl)
                .into(binding.ivDetailPhoto)
            binding.tvDetailName.text = story.name
            binding.tvDetailDescription.text = story.description
        }
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
    }
}
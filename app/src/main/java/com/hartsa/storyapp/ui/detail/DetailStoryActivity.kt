package com.hartsa.storyapp.ui.detail

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.hartsa.storyapp.data.UserPreference
import com.hartsa.storyapp.data.response.Story
import com.hartsa.storyapp.databinding.ActivityDetailStoryBinding
import com.hartsa.storyapp.ui.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "User")

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding
    private lateinit var viewModel: DetailViewModel
    private var storyId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        storyId = intent.getStringExtra(STORY_ID)
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[DetailViewModel::class.java]

        viewModel.getUser().observe(this) { user ->
            storyId?.let { id ->

                viewModel.getDetailStory(user.token, id)
            }
        }

        viewModel.detailStory.observe(this) { detailStory ->
            setDetailStory(detailStory)
        }
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setDetailStory(detailStory: Story) {
        binding.apply {
            Glide.with(root.context)
                .load(detailStory.photoUrl)
                .into(ivDetailPhoto)
            tvDetailName.text = detailStory.name
            tvDetailDescription.text = detailStory.description
            tvDetailDate.text = detailStory.createdAt
        }
    }

    companion object {
        const val STORY_ID = "id"
    }
}
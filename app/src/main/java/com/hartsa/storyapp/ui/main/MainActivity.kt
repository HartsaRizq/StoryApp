package com.hartsa.storyapp.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hartsa.storyapp.R
import com.hartsa.storyapp.data.UserPreference
import com.hartsa.storyapp.data.response.ListStoryItem
import com.hartsa.storyapp.databinding.ActivityMainBinding
import com.hartsa.storyapp.ui.ViewModelFactory
import com.hartsa.storyapp.ui.auth.login.LoginActivity
import com.hartsa.storyapp.ui.auth.login.LoginViewModel
import com.hartsa.storyapp.ui.story.StoryActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "User")

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupMenuItems()
        recyclerViewSetting()
        addStory()
        mainViewModel.storyList.observe(this) {listStory ->
            setStory(listStory)
        }
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun addStory() {
        binding.buttonAdd.setOnClickListener {
            val intent = Intent(this, StoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]


        loginViewModel.getUser().observe(this) { user ->
            if (user.userId.isEmpty()) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                mainViewModel.getListStory(user.token)
            }
        }
    }

    private fun setupMenuItems() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.logout -> {
                    mainViewModel.logout()
                    true
                }
                R.id.language -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    true
                }
                else -> false
            }
        }
    }

    private fun recyclerViewSetting(){
        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStory.addItemDecoration(itemDecoration)
    }

    private fun setStory(listStory: List<ListStoryItem>) {
        val adapter = StoryAdapter(listStory as ArrayList<ListStoryItem>)
        binding.rvStory.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
package com.hartsa.storyapp.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.hartsa.storyapp.data.ApiConfig
import com.hartsa.storyapp.data.UserPreference
import com.hartsa.storyapp.data.response.DetailStoryResponse
import com.hartsa.storyapp.data.response.LoginResult
import com.hartsa.storyapp.data.response.Story
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val pref: UserPreference) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _detailStory = MutableLiveData<Story>()
    val detailStory : LiveData<Story> = _detailStory

    fun getUser(): LiveData<LoginResult> {
        return pref.getUser().asLiveData()
    }


    fun getDetailStory(token: String, id: String) {
        _isLoading.value = true
        val apiService = ApiConfig().getApiService()
        val detailStoryRequest = apiService.getDetailStory(bearer = "Bearer $token", id)
        detailStoryRequest.enqueue(object : Callback<DetailStoryResponse> {
            override fun onResponse(
                call: Call<DetailStoryResponse>,
                response: Response<DetailStoryResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailStory.value = response.body()?.story
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                _isLoading.value = true
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "DetailViewModel"
    }
}
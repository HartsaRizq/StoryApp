package com.hartsa.storyapp.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hartsa.storyapp.data.ApiConfig
import com.hartsa.storyapp.data.UserPreference
import com.hartsa.storyapp.data.response.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    private val _registerSuccess = MutableLiveData<Boolean>()
    val registerSuccess: LiveData<Boolean> get() = _registerSuccess

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun register(name:String, email: String, password: String) {
        _isLoading.value = true
        val apiService = ApiConfig().getApiService()
        val registerRequest = apiService.registerAcc(name, email, password)

        registerRequest.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                _registerSuccess.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        _toastMessage.value = responseBody.message

                        _registerSuccess.value = true
                        _isLoading.value = false
                    }
                } else {
                    _toastMessage.value = response.message()
                }
                _isLoading.value = false
            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _toastMessage.value = t.message
                _isLoading.value = false
            }
        })

    }
}
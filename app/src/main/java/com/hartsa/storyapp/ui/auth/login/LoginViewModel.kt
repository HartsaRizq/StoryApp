package com.hartsa.storyapp.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hartsa.storyapp.data.ApiConfig
import com.hartsa.storyapp.data.UserPreference
import com.hartsa.storyapp.data.response.LoginResponse
import com.hartsa.storyapp.data.response.LoginResult
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: UserPreference) : ViewModel() {

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> get() = _loginSuccess

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun saveUser(userName: String, userId: String, userToken:String) {
        viewModelScope.launch {
            pref.saveUser(userName, userId, userToken)
        }
    }

    fun getUser(): LiveData<LoginResult> {
        return pref.getUser().asLiveData()
    }

    fun login(email: String, password: String) {
        _isLoading.value = true
        val apiService = ApiConfig().getApiService()
        val loginRequest = apiService.loginAcc(email, password)

        loginRequest.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _loginSuccess.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        _toastMessage.value = responseBody.message
                        _loginSuccess.value = true

                        val loginResult = responseBody.loginResult
                        saveUser(loginResult.name, loginResult.userId, loginResult.token)
                    }
                } else {
                    _toastMessage.value = response.message()
                }
                _isLoading.value = false
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _toastMessage.value = t.message
            }
        })

    }
}
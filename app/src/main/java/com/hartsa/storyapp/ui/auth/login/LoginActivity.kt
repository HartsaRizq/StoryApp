package com.hartsa.storyapp.ui.auth.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.hartsa.storyapp.R
import com.hartsa.storyapp.data.UserPreference
import com.hartsa.storyapp.databinding.ActivityLoginBinding
import com.hartsa.storyapp.ui.ViewModelFactory
import com.hartsa.storyapp.ui.auth.register.RegisterActivity
import com.hartsa.storyapp.ui.main.MainActivity

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("User")

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        login()
        playAnimation()


        binding.tvRegister.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.logo , View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val logoTV = ObjectAnimator.ofFloat(binding.tvContinueLogo, View.ALPHA, 1f).setDuration(500)
        val emailEDT = ObjectAnimator.ofFloat(binding.edtEmail, View.ALPHA, 1f).setDuration(500)
        val passwordEDT = ObjectAnimator.ofFloat(binding.edtPassword, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val register = ObjectAnimator.ofFloat(binding.llRegister, View.ALPHA, 1f).setDuration(500)


        AnimatorSet().apply {
            playSequentially(logoTV, emailEDT, passwordEDT, login, register)
            startDelay = 500
        }.start()
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
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, ViewModelFactory(UserPreference.getInstance(dataStore)))[LoginViewModel::class.java]
        viewModel.toastMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
        viewModel.loginSuccess.observe(this) { success ->
            if (success) {
                navigateToMainActivity()
            }
        }
    }

    private fun login() {
        binding.btnLogin.setOnClickListener{
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            val emptyField = getString(R.string.empty_field)
            val passwordReq = getString(R.string.password_req)
            val emailReq = getString(R.string.email_req)
            when {
                email.isEmpty() -> {
                    binding.edtEmail.error = emptyField
                }
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->{
                    binding.edtEmail.error = emailReq
                }
                password.isEmpty() -> {
                    binding.edtPassword.error = emptyField
                }
                password.length < 8 -> {
                    binding.edtPassword.error = passwordReq
                }
                else -> {
                    viewModel.login(email, password)
                }
            }
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarLogin.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
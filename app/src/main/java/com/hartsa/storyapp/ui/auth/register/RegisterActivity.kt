package com.hartsa.storyapp.ui.auth.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.hartsa.storyapp.R
import com.hartsa.storyapp.data.UserPreference
import com.hartsa.storyapp.databinding.ActivityRegisterBinding
import com.hartsa.storyapp.ui.ViewModelFactory
import com.hartsa.storyapp.ui.auth.login.LoginActivity
import com.hartsa.storyapp.ui.auth.login.dataStore


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        register()
        navigateToLoginActivity()
        playAnimation()

        binding.tvLogin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.logo , View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val logoTV = ObjectAnimator.ofFloat(binding.tvContinueRegister, View.ALPHA, 1f).setDuration(500)
        val nameEDT = ObjectAnimator.ofFloat(binding.edtName, View.ALPHA, 1f).setDuration(500)
        val emailEDT = ObjectAnimator.ofFloat(binding.edtEmail, View.ALPHA, 1f).setDuration(500)
        val passwordEDT = ObjectAnimator.ofFloat(binding.edtPassword, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)
        val register = ObjectAnimator.ofFloat(binding.llLogin, View.ALPHA, 1f).setDuration(500)


        AnimatorSet().apply {
            playSequentially(logoTV, nameEDT, emailEDT, passwordEDT, login, register)
            start()
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, ViewModelFactory(UserPreference.getInstance(dataStore)))[RegisterViewModel::class.java]
        viewModel.toastMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun register() {
        binding.btnRegister.setOnClickListener{
            val name = binding.edtName.text.toString()
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            val emptyField = getString(R.string.empty_field)
            val passwordReq = getString(R.string.password_req)
            val emailReq = getString(R.string.email_req)
            when {
                name.isEmpty() ->{
                    binding.edtName.error = emptyField
                }
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
                    viewModel.register(name, email, password)
                }
            }
        }
    }

    private fun navigateToLoginActivity() {
        viewModel.registerSuccess.observe(this) { success ->
            if (success) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
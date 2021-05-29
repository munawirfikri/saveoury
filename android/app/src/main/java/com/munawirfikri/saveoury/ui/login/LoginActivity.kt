package com.munawirfikri.saveoury.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns.*
import android.view.View
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import com.munawirfikri.saveoury.R
import com.munawirfikri.saveoury.data.source.local.SharedPreference
import com.munawirfikri.saveoury.databinding.ActivityLoginBinding
import com.munawirfikri.saveoury.ui.main.MainActivity
import com.munawirfikri.saveoury.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var sharedPref: SharedPreference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = SharedPreference(this)

        if(sharedPref.getValueString("token") != null){
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnDaftar.setOnClickListener(this)
        binding.btnMasuk.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_masuk -> {
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty()){
                    if(!EMAIL_ADDRESS.matcher(email).matches()){
                        binding.etEmail.error = getString(R.string.email_not_valid)
                    }else{
                        loginViewModel.loginUser(email, password)
                        loginViewModel.user.observe(this, {user ->
                            if (user != null) {
                                sharedPref.run {
                                    save("token", user.tokenType.toString() + " " + user.accessToken.toString())
                                    save("name", user.user.name.toString())
                                    save("email", user.user.email.toString())
                                    save("address", user.user.address.toString())
                                    save("phoneNumber", user.user.phoneNumber.toString())
                                    save("city", user.user.city.toString())
                                    save("photo", user.user.profilePhotoUrl.toString())
                                }
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        })
                        loginViewModel.itExist.observe(this, {
                            if(it == false){
                                Snackbar.make(binding.root, "Pengguna tidak ditemukan", Snackbar.LENGTH_SHORT).show()
                            }
                        })
                        loginViewModel.isLoading.observe(this, {
                            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
                        })
                    }
                }else {
                    Snackbar.make(binding.root, "Data tidak boleh kosong!", Snackbar.LENGTH_SHORT).show()
                }
            }
            R.id.btn_daftar -> {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
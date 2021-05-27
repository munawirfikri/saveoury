package com.munawirfikri.saveoury.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.munawirfikri.saveoury.R
import com.munawirfikri.saveoury.databinding.ActivityRegisterBinding
import com.munawirfikri.saveoury.ui.register.fragment.RegisterFragment

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mFragmentManager = supportFragmentManager
        val mRegisterFragment = RegisterFragment()
        val fragment = mFragmentManager.findFragmentByTag(RegisterFragment::class.java.simpleName)

        if (fragment !is RegisterFragment){
            Log.d("RegisterActivity", "Fragment Name: " + RegisterFragment::class.java.simpleName)
            mFragmentManager
                .beginTransaction()
                .add(R.id.register_frame_container, mRegisterFragment, RegisterFragment::class.java.simpleName)
                .commit()
        }

    }


}

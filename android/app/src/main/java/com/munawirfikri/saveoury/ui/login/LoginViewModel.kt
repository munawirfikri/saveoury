package com.munawirfikri.saveoury.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.munawirfikri.saveoury.data.source.remote.network.ApiConfig
import com.munawirfikri.saveoury.data.source.remote.response.UserData
import com.munawirfikri.saveoury.data.source.remote.response.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel: ViewModel() {

    private val _user = MutableLiveData<UserData>()
    val user: LiveData<UserData> =  _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _itExist = MutableLiveData<Boolean>()
    val itExist: LiveData<Boolean> = _itExist

    fun loginUser(email: String, password: String){
        _isLoading.value = true
        _itExist.value = true
        val client = ApiConfig.provideSaveouryApiService().loginUser(email, password)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _user.value = response.body()?.data
                }else{
                    Log.e("Login", "gagal: ${response.message()}")
                    _itExist.value = false
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                _itExist.value = false
                Log.e("Login", "onFailure: ${t.message.toString()}")
            }
        })
    }
}
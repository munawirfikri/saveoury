package com.munawirfikri.saveoury.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.munawirfikri.saveoury.BuildConfig
import com.munawirfikri.saveoury.data.source.remote.network.ApiConfig
import com.munawirfikri.saveoury.data.source.remote.response.UserResponse
import com.munawirfikri.saveoury.data.source.remote.response.UserEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@FlowPreview
@ExperimentalCoroutinesApi
class RegisterViewModel: ViewModel() {

    private val _user = MutableLiveData<UserEntity>()
    val user: LiveData<UserEntity> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _itExist = MutableLiveData<Boolean>()
    val itExist: LiveData<Boolean> = _itExist

    private val accessToken = BuildConfig.MAPBOX_TOKEN
    val queryChannel = BroadcastChannel<String>(Channel.CONFLATED)

    val searchResult = queryChannel.asFlow()
        .debounce(300)
        .distinctUntilChanged()
        .filter {
            it.trim().isNotEmpty()
        }
        .mapLatest {
            ApiConfig.provideMapboxApiService().getCity(it, accessToken).features
        }
        .asLiveData()

    fun registerUser(
        name: String,
        email: String,
        password: String,
        phoneNumber: String,
        address: String,
        city: String
                     ){
        _isLoading.value = true
        val client = ApiConfig
            .provideSaveouryApiService()
            .registerNewUser(name, email, password, password, phoneNumber, address, city)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _user.value = response.body()?.data?.user
                }else{
                    _itExist.value = false
                    Log.e("Register", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                _itExist.value = false
                Log.e("Register", "onFailure: ${t.message.toString()}")
            }
        })
    }
}
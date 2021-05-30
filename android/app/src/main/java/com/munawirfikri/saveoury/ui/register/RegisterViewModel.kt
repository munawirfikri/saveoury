package com.munawirfikri.saveoury.ui.register

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.munawirfikri.saveoury.BuildConfig
import com.munawirfikri.saveoury.data.source.remote.network.ApiConfig
import com.munawirfikri.saveoury.data.source.remote.response.UserResponse
import com.munawirfikri.saveoury.data.source.remote.response.UserData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

@FlowPreview
@ExperimentalCoroutinesApi
class RegisterViewModel: ViewModel() {

    private val _user = MutableLiveData<UserData>()
    val user: LiveData<UserData> = _user

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

    @SuppressLint("SdCardPath")
    fun uploadPhotoUser(authorization: String, path: String){
        val file = File("/data/data/com.munawirfikri.saveoury/cache/user/$path")
        val requestBody = file.asRequestBody("*/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, requestBody)

        val client = ApiConfig.provideSaveouryApiService().uploadPhotoUser(authorization, body)

        client.enqueue(object : Callback<UserResponse>{
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                Log.e("Photo", response.body().toString())
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e("Photo", "onFailure: ${t.message.toString()}")
            }
        })
    }

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
                    _user.value = response.body()?.data
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
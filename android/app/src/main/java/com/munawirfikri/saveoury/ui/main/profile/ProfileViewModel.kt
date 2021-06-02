package com.munawirfikri.saveoury.ui.main.profile

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.munawirfikri.saveoury.data.source.remote.network.ApiConfig
import com.munawirfikri.saveoury.data.source.remote.response.FoodPostItem
import com.munawirfikri.saveoury.data.source.remote.response.FoodPostResponse
import com.munawirfikri.saveoury.data.source.remote.response.UserData
import com.munawirfikri.saveoury.data.source.remote.response.UserResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ProfileViewModel : ViewModel() {

    private val _user = MutableLiveData<UserData>()
    val user: LiveData<UserData> =  _user

    private val _foodPost = MutableLiveData<List<FoodPostItem>>()
    val foodPost: LiveData<List<FoodPostItem>> = _foodPost

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading



    fun getFoodPostByUserId(city: String, userId: String){
        _isLoading.value = true
        val client = ApiConfig.provideSaveouryApiService().getFoodPostByUserId(city, userId)
        client.enqueue(object : Callback<FoodPostResponse> {
            override fun onResponse(
                call: Call<FoodPostResponse>,
                response: Response<FoodPostResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _foodPost.value = response.body()?.items
                }else{
                    Log.e("ProfileViewModel", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<FoodPostResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("ProfileViewModel", "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun updateUser(authorization: String, email: String, name: String, address: String, phoneNumber: String, city: String){
        val client = ApiConfig.provideSaveouryApiService()
            .updateUser(authorization, email, name, address, phoneNumber, city)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful){
                    _user.value = response.body()?.data
                }else{
                    Log.e("Profile", "gagal: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e("Profile", "gagal: ${t.message}")
            }
        })
    }
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

}
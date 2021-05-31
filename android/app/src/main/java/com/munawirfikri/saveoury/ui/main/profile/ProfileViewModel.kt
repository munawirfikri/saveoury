package com.munawirfikri.saveoury.ui.main.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.munawirfikri.saveoury.data.source.remote.network.ApiConfig
import com.munawirfikri.saveoury.data.source.remote.response.FoodPostItem
import com.munawirfikri.saveoury.data.source.remote.response.FoodPostResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel : ViewModel() {

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


}
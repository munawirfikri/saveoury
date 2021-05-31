package com.munawirfikri.saveoury.ui.main.post

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.munawirfikri.saveoury.data.source.remote.network.ApiConfig
import com.munawirfikri.saveoury.data.source.remote.response.FoodPostItem
import com.munawirfikri.saveoury.data.source.remote.response.FoodPostResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class PostViewModel : ViewModel() {

    private val _foodPost = MutableLiveData<FoodPostItem>()
    private val foodPost: LiveData<FoodPostItem> = _foodPost

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun addFoodPost(
        authorization: String,path: String,
        foodName: String, foodDesc: String,
        foodCategory: String, foodLocation: String,
        isVerifiedFood: String, isAvailableFood: String){

        val file = File("/data/data/com.munawirfikri.saveoury/cache/foodpost/$path")
        val requestBody = file.asRequestBody("*/*".toMediaTypeOrNull())
        val image = MultipartBody.Part.createFormData("file", file.name, requestBody)
        val name = MultipartBody.Part.createFormData("food_name", foodName)
        val desc = MultipartBody.Part.createFormData("food_desc", foodDesc)
        val category = MultipartBody.Part.createFormData("category", foodCategory)
        val location = MultipartBody.Part.createFormData("location", foodLocation)
        val isVerified = MultipartBody.Part.createFormData("is_verified", isVerifiedFood)
        val isAvailable = MultipartBody.Part.createFormData("is_available", isAvailableFood)

        val client = ApiConfig.provideSaveouryApiService().addFoodPost(
            authorization,
            image,
            name,
            desc,
            category,
            location,
            isVerified,
            isAvailable
        )

        client.enqueue(object : Callback<FoodPostResponse>{
            override fun onResponse(
                call: Call<FoodPostResponse>,
                response: Response<FoodPostResponse>
            ) {
                if (response.isSuccessful){

                }else{
                    Log.e("PostViewModel", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<FoodPostResponse>, t: Throwable) {
                Log.e("PostViewModel", "onFailure: ${t.message}")
            }
        })

    }

}
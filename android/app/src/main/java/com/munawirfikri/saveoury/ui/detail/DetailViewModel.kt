package com.munawirfikri.saveoury.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.munawirfikri.saveoury.data.source.remote.network.ApiConfig
import com.munawirfikri.saveoury.data.source.remote.response.FoodPostItem
import com.munawirfikri.saveoury.data.source.remote.response.FoodPostResponse
import com.munawirfikri.saveoury.data.source.remote.response.Transaction
import com.munawirfikri.saveoury.data.source.remote.response.TransactionResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {
    private val _detail = MutableLiveData<FoodPostItem>()
    val detail: LiveData<FoodPostItem> = _detail

    private val _transaction = MutableLiveData<List<Transaction>>()
    val transaction: LiveData<List<Transaction>> = _transaction

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getDetailFood(id: String){
        _isLoading.value = true
        val client = ApiConfig.provideSaveouryApiService().getDetailFoodById(id)
        client.enqueue(object: Callback<FoodPostResponse> {
            override fun onResponse(
                call: Call<FoodPostResponse>,
                response: Response<FoodPostResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _detail.value = response.body()!!.items[0]
                }
            }

            override fun onFailure(call: Call<FoodPostResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }
    fun setFoodPostStatus(authorization: String, id: String, isAvailable: Int){
        val client = ApiConfig.provideSaveouryApiService().setFoodPostStatus(authorization, id, isAvailable)
        client.enqueue(object : Callback<FoodPostResponse>{
            override fun onResponse(
                call: Call<FoodPostResponse>,
                response: Response<FoodPostResponse>
            ) {

            }
            override fun onFailure(call: Call<FoodPostResponse>, t: Throwable) {
            }
        })
    }

    fun getTransaction(ownerId: String, foodId: String){
        _isLoading.value = true
        val client = ApiConfig.provideSaveouryApiService().getTransactionByOwnerIdAndFoodId(ownerId, foodId)
        client.enqueue(object : Callback<TransactionResponse> {
            override fun onResponse(
                call: Call<TransactionResponse>,
                response: Response<TransactionResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _transaction.value = response.body()?.data
                }
            }

            override fun onFailure(call: Call<TransactionResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }
    fun addTransaction(authorization: String, foodId: String, ownerId: String, recipientId: String){
        val client = ApiConfig.provideSaveouryApiService().addTransaction(authorization, foodId, ownerId, recipientId)
        client.enqueue(object : Callback<TransactionResponse> {
            override fun onResponse(
                call: Call<TransactionResponse>,
                response: Response<TransactionResponse>
            ) {

            }
            override fun onFailure(call: Call<TransactionResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

}
package com.munawirfikri.saveoury.data.source.remote.network

import com.munawirfikri.saveoury.data.source.remote.response.FoodPostResponse
import com.munawirfikri.saveoury.data.source.remote.response.PlaceResponse
import com.munawirfikri.saveoury.data.source.remote.response.UserResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("mapbox.places/{query}.json")
    suspend fun getCity(
        @Path("query") query: String,
        @Query("access_token") accessToken: String,
        @Query("autocomplete") autoComplete: Boolean = true,
        @Query("types") types: String = "place"
    ): PlaceResponse

    @GET("foodpost")
    fun getFoodPost(
        @Query("location") city: String? = "Pekanbaru"
    ): Call<List<FoodPostResponse>>

    @FormUrlEncoded
    @POST("register")
    fun registerNewUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("password_confirmation") passwordConfirmation: String,
        @Field("phoneNumber") phoneNumber: String,
        @Field("address") address: String,
        @Field("city") city: String
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("user/photo")
    fun uploadPhotoUser(
        @Part("file") file: MultipartBody.Part
    ): Call<UserResponse>
}
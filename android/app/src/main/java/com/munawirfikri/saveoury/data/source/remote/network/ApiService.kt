package com.munawirfikri.saveoury.data.source.remote.network

import com.munawirfikri.saveoury.data.source.remote.response.FoodPostResponse
import com.munawirfikri.saveoury.data.source.remote.response.ImageResponse
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
    ): Call<FoodPostResponse>

    @GET("foodpost")
    fun getFoodPostByUserId(
        @Query("location") city: String? = "Pekanbaru",
        @Query("id_user") id_user: String
    ): Call<FoodPostResponse>

    @Multipart
    @POST("foodpost/add")
    fun addFoodPost(
        @Header("Authorization") authorization: String,
        @Part file: MultipartBody.Part,
        @Part foodName: MultipartBody.Part,
        @Part foodDesc: MultipartBody.Part,
        @Part foodCategory: MultipartBody.Part,
        @Part foodLocation: MultipartBody.Part,
        @Part IsVerifiedFood: MultipartBody.Part,
        @Part IsAvailableFood: MultipartBody.Part
    ): Call<FoodPostResponse>

    @Multipart
    @POST("/")
    fun predictImage(
        @Part file: MultipartBody.Part
    ): Call<ImageResponse>

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

    @Multipart
    @POST("user/photo")
    fun uploadPhotoUser(
        @Header("Authorization") authorization: String,
        @Part file: MultipartBody.Part
    ): Call<UserResponse>
}
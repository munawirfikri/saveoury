package com.munawirfikri.saveoury.data.source.remote.network

import com.munawirfikri.saveoury.data.source.remote.response.FoodPostResponse
import com.munawirfikri.saveoury.data.source.remote.response.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

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
}
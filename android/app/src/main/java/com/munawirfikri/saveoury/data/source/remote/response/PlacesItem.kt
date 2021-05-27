package com.munawirfikri.saveoury.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class PlacesItem(
    @field:SerializedName("text")
    val placeName: String
)
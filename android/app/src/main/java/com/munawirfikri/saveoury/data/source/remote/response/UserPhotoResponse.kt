package com.munawirfikri.saveoury.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class UserPhotoResponse(

	@field:SerializedName("data")
	val data: List<String>,

	@field:SerializedName("meta")
	val meta: Meta
)

data class Meta(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

package com.munawirfikri.saveoury.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
	@field:SerializedName("data")
	val data: Data
)

data class Data(

	@field:SerializedName("access_token")
	val accessToken: String? = null,

	@field:SerializedName("token_type")
	val tokenType: String? = null,

	@field:SerializedName("user")
	val user: UserEntity
)

data class UserEntity(

	@field:SerializedName("profile_photo_url")
	val profilePhotoUrl: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("phoneNumber")
	val phoneNumber: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: Int? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("roles")
	val roles: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: Int? = null,

	@field:SerializedName("email_verified_at")
	val emailVerifiedAt: Any? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("profile_photo_path")
	val profilePhotoPath: Any? = null,

	@field:SerializedName("email")
	val email: String? = null
)

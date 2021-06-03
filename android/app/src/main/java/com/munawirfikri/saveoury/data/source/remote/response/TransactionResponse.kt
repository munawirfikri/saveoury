package com.munawirfikri.saveoury.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class TransactionResponse(

	@field:SerializedName("data")
	val data: List<Transaction>
)

data class FoodPost(

	@field:SerializedName("picturePath")
	val picturePath: String,

	@field:SerializedName("food_name")
	val foodName: String,

	@field:SerializedName("updated_at")
	val updatedAt: Long,

	@field:SerializedName("created_at")
	val createdAt: Long,

	@field:SerializedName("food_desc")
	val foodDesc: String,

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("id_user")
	val idUser: Int,

	@field:SerializedName("category")
	val category: String,

	@field:SerializedName("is_verified")
	val isVerified: Int,

	@field:SerializedName("deleted_at")
	val deletedAt: Any,

	@field:SerializedName("is_available")
	val isAvailable: Int
)

data class Transaction(

	@field:SerializedName("updated_at")
	val updatedAt: Long,

	@field:SerializedName("owner_id")
	val ownerId: Int,

	@field:SerializedName("created_at")
	val createdAt: Long,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("food_id")
	val foodId: Int,

	@field:SerializedName("deleted_at")
	val deletedAt: Any,

	@field:SerializedName("user")
	val user: User,

	@field:SerializedName("recipient_id")
	val recipientId: Int,

	@field:SerializedName("status")
	val status: String,

	@field:SerializedName("food_post")
	val foodPost: FoodPost
)

package com.munawirfikri.saveoury.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ImageResponse(

	@field:SerializedName("prediksi")
	val prediksi: Int
)

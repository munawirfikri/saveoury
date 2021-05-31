package com.munawirfikri.saveoury.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.munawirfikri.saveoury.R
import com.munawirfikri.saveoury.data.source.remote.response.FoodPostItem
import com.munawirfikri.saveoury.databinding.ItemPostBinding

class HomeAdapter: RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    private var listData = ArrayList<FoodPostItem>()

    fun setData(newListData: List<FoodPostItem>?){
        if(newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    class HomeViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root)
    {
        fun bind(foodPostItem: FoodPostItem){
            with(binding){
                namaUser.text = foodPostItem.dataUser.name
                foodStatus.text = if(foodPostItem.isAvailable==true) "Tersedia" else "Tidak tersedia"
                tvFoodDesc.text = foodPostItem.foodDesc
                tvFoodName.text = foodPostItem.foodName
                val foodImage = foodPostItem.picturePath?.substring(4)
                Glide.with(itemView.context)
                    .load("https$foodImage")
                    .centerCrop()
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error))
                    .into(ivFoodImage)

                Glide.with(itemView.context)
                    .load(foodPostItem.dataUser.profilePhotoUrl)
                    .centerCrop()
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error))
                    .into(profileImage)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val itemPostBinding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(itemPostBinding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    override fun getItemCount() = listData.size
}
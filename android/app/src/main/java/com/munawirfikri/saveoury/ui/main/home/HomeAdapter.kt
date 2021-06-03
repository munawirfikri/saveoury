package com.munawirfikri.saveoury.ui.main.home

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.munawirfikri.saveoury.R
import com.munawirfikri.saveoury.data.source.remote.response.FoodPostItem
import com.munawirfikri.saveoury.databinding.ItemPostBinding
import com.munawirfikri.saveoury.ui.detail.DetailActivity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeAdapter: RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    private var listData = ArrayList<FoodPostItem>()

    fun setData(newListData: List<FoodPostItem>?){
        if(newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    @Suppress("DEPRECATION")
    class HomeViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root)
    {
        @SuppressLint("SimpleDateFormat", "SetTextI18n", "UseCompatLoadingForColorStateLists")
        fun bind(foodPostItem: FoodPostItem){
            with(binding){
                tvNamaUser.text = foodPostItem.dataUser.name
                tvAlamatUser.text = foodPostItem.dataUser.address
                val date = Date(foodPostItem.createdAt!!)
                val format = SimpleDateFormat("dd/MM/yyyy (HH:mm)")
                tvFoodPostTime.text = "Diposting pada tanggal " + format.format(date)
                tvFoodDesc.text = foodPostItem.foodDesc
                tvFoodName.text = foodPostItem.foodName
                 if(foodPostItem.isAvailable == 1){
                     tvItemStatus.text = "Tersedia"
                     tvItemStatus.setTextColor(root.resources.getColorStateList(R.color.primary_variant))
                 }else{
                     tvItemStatus.text = "Tidak Tersedia"
                     tvItemStatus.setTextColor(Color.GRAY)
                 }

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_ID, foodPostItem.id.toString())
                    itemView.context.startActivity(intent)
                }
                Glide.with(itemView.context)
                    .load(foodPostItem.picturePath)
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
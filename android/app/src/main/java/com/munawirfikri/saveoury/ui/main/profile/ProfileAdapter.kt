package com.munawirfikri.saveoury.ui.main.profile

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.munawirfikri.saveoury.R
import com.munawirfikri.saveoury.data.source.remote.response.FoodPostItem
import com.munawirfikri.saveoury.databinding.ItemMyPostBinding
import com.munawirfikri.saveoury.ui.detail.DetailActivity

class ProfileAdapter: RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {

    private var listData = ArrayList<FoodPostItem>()

    fun setData(newListData: List<FoodPostItem>?){
        if(newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    class ProfileViewHolder(private val binding: ItemMyPostBinding) : RecyclerView.ViewHolder(binding.root)
    {
        fun bind(foodPostItem: FoodPostItem){
            with(binding){
                val foodImage = foodPostItem.picturePath?.substring(4)
                Glide.with(itemView.context)
                    .load("https$foodImage")
                    .centerCrop()
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error))
                    .into(imgPoster)
                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_ID, foodPostItem.id.toString())
                    itemView.context.startActivity(intent)
                }
                binding.tvItemFoodname.text = foodPostItem.foodName
                binding.tvItemStatus.text = if(foodPostItem.isAvailable == true) "Status: Tersedia" else "Status: Selesai"
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val itemPostBinding = ItemMyPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileViewHolder(itemPostBinding)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    override fun getItemCount() = listData.size
}
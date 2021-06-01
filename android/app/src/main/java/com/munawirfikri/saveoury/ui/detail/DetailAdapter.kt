package com.munawirfikri.saveoury.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.munawirfikri.saveoury.R
import com.munawirfikri.saveoury.data.source.local.SharedPreference
import com.munawirfikri.saveoury.data.source.remote.network.ApiConfig
import com.munawirfikri.saveoury.data.source.remote.response.Transaction
import com.munawirfikri.saveoury.data.source.remote.response.TransactionResponse
import com.munawirfikri.saveoury.databinding.ItemRequestBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class DetailAdapter: RecyclerView.Adapter<DetailAdapter.DetailViewHolder>() {

    private var listData = ArrayList<Transaction>()
    fun setData(newListData: List<Transaction>?){
        if(newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    class DetailViewHolder(private val binding: ItemRequestBinding) : RecyclerView.ViewHolder(binding.root)
    {
        fun bind(item: Transaction){
            val sharedPreference = SharedPreference(itemView.context)
            val token = sharedPreference.getValueString("token")
            with(binding){
                tvNameRecipient.text = item.user.name
                tvAddressRecipient.text = item.user.address
                Glide.with(itemView.context)
                    .load(item.user.profilePhotoUrl)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error))
                    .into(imgProfile)
                tbResponse.isChecked = (item.status == "Diterima")

                tbResponse.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        val client = ApiConfig.provideSaveouryApiService().updateTransaction(token.toString(), item.id.toString(), item.ownerId.toString(), "Diterima")
                        client.enqueue(object : Callback<TransactionResponse> {
                            override fun onResponse(
                                call: Call<TransactionResponse>,
                                response: Response<TransactionResponse>
                            ) {

                            }

                            override fun onFailure(call: Call<TransactionResponse>, t: Throwable) {

                            }
                        })
                        Snackbar.make(itemView, "Permintaan diizinkan", Snackbar.LENGTH_SHORT).show()
                    }else {
                        val client = ApiConfig.provideSaveouryApiService().updateTransaction(token.toString(), item.id.toString(), item.ownerId.toString(), "Diminta")
                        client.enqueue(object : Callback<TransactionResponse> {
                            override fun onResponse(
                                call: Call<TransactionResponse>,
                                response: Response<TransactionResponse>
                            ) {

                            }

                            override fun onFailure(call: Call<TransactionResponse>, t: Throwable) {

                            }
                        })
                        Snackbar.make(itemView, "Permintaan ditangguhkan", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val itemRequestBinding = ItemRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetailViewHolder(itemRequestBinding)
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    override fun getItemCount() = listData.size

}
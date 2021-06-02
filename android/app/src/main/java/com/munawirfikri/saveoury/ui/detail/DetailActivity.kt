package com.munawirfikri.saveoury.ui.detail

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.munawirfikri.saveoury.R
import com.munawirfikri.saveoury.data.source.local.SharedPreference
import com.munawirfikri.saveoury.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModels()
    private var detailAdapter: DetailAdapter? = null
    private lateinit var sharedPref: SharedPreference

    companion object {
        const val EXTRA_ID = "extra_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        sharedPref = SharedPreference(this)
        val authUserId = sharedPref.getValueString("id_user")
        setContentView(binding.root)

        val extras = intent.extras
        if (extras!=null){
            val extraId = extras.getString(EXTRA_ID)
            Log.d("extraID", extraId.toString())
            if(extraId != null){
                detailViewModel.getDetailFood(extraId)
                detailViewModel.detail.observe(this, {
                    binding.tvFoodName.text = it.foodName
                    binding.tvFoodOwner.text = it.dataUser.name
                    binding.notRequestYet.btnAddRequest.setOnClickListener(this)
                    Glide.with(this)
                        .load(it.dataUser.profilePhotoUrl)
                        .centerCrop()
                        .apply(
                            RequestOptions.placeholderOf(R.drawable.ic_loading)
                                .error(R.drawable.ic_error))
                        .into(binding.imgProfileUser)
                    Glide.with(this)
                        .load(it.picturePath)
                        .centerCrop()
                        .apply(
                            RequestOptions.placeholderOf(R.drawable.ic_loading)
                                .error(R.drawable.ic_error))
                        .into(binding.imgFood)
                    val ownerId = it.idUser
                    if (it.isAvailable == false){
                        binding.closeRequest.root.visibility = View.VISIBLE
                        binding.ownerSide.root.visibility = View.GONE
                        binding.notRequestYet.root.visibility = View.GONE
                        binding.waitingResponse.root.visibility = View.GONE
                        binding.recipientSide.root.visibility = View.GONE
                    }else{
                        detailViewModel.getTransaction(ownerId.toString() , extraId)
                        detailViewModel.transaction.observe(this, { transactionList ->
                            transactionList.map { transaction ->
                                if (authUserId == transaction.recipientId.toString()) {
                                    if (transaction.status == "Diterima"){
                                        binding.recipientSide.root.visibility = View.VISIBLE
                                        binding.recipientSide.tvAddressUser.text = it.dataUser.address
                                        binding.recipientSide.tvPhoneNumberUser.text = it.dataUser.phoneNumber
                                        val phoneNumber = it.dataUser.phoneNumber.toString()
                                        binding.recipientSide.tvPhoneNumberUser.setOnClickListener {
                                            val callIntent: Intent = Uri.parse("tel:" +phoneNumber).let { number ->
                                                Intent(Intent.ACTION_DIAL, number)
                                            }
                                            startActivity(callIntent)
                                        }
                                    }
                                    binding.waitingResponse.root.visibility = if (transaction.status == "Diminta") View.VISIBLE else View.GONE
                                }else{
                                    binding.notRequestYet.root.visibility = View.VISIBLE
                                }
                            }
                            binding.notRequestYet.root.visibility = if (transactionList.isEmpty() && authUserId != ownerId.toString()) View.VISIBLE else View.GONE
                        })
                    }
                    if (authUserId.toString() == ownerId.toString()) {
                        binding.ownerSide.root.visibility = View.VISIBLE
                        binding.notRequestYet.root.visibility = View.GONE
                        binding.waitingResponse.root.visibility = View.GONE
                        binding.recipientSide.root.visibility = View.GONE
                        binding.switchBerbagi.visibility = View.VISIBLE
                        binding.closeRequest.root.visibility = View.GONE
                        binding.switchBerbagi.isChecked = it.isAvailable!!

                        binding.switchBerbagi.setOnCheckedChangeListener { _, isChecked ->
                            val token = sharedPref.getValueString("token")
                            if (isChecked){
                                detailViewModel.setFoodPostStatus(token.toString(), it.id.toString(), true)
                                Snackbar.make(binding.root, "Postingan untuk makanan ini diaktifkan", Snackbar.LENGTH_SHORT).show()
                                finish()
                                startActivity(intent)
                            }else{
                                detailViewModel.setFoodPostStatus(token.toString(), it.id.toString(), false)
                                Snackbar.make(binding.root, "Postingan untuk makanan ini dinonaktifkan", Snackbar.LENGTH_SHORT).show()
                                finish()
                                startActivity(intent)
                            }
                        }

                    }else{
                        binding.switchBerbagi.visibility = View.GONE
                    }
                })
                detailViewModel.isLoading.observe(this, {
                    binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
                })
            }
        }
        detailAdapter = DetailAdapter()
        detailViewModel.transaction.observe(this, {
            detailAdapter?.setData(it)
            detailAdapter?.notifyDataSetChanged()
        })

        with(binding.ownerSide.rvRequest){
            this.layoutManager = LinearLayoutManager(context)
            this.setHasFixedSize(true)
            this.adapter = detailAdapter
        }
        binding.ivBack.setOnClickListener(this)
        Log.d("detailAdapter", detailAdapter?.itemCount.toString())
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.iv_back -> finish()
            R.id.btn_add_request -> {
                val authUser = sharedPref.getValueString("id_user")
                val token = sharedPref.getValueString("token")
                detailViewModel.detail.observe(this, {
                    detailViewModel.addTransaction(token.toString(), it.id.toString(), it.idUser.toString(), authUser.toString())
                    binding.waitingResponse.root.visibility = View.VISIBLE
                    binding.notRequestYet.root.visibility = View.GONE
                })
            }
        }
    }
}
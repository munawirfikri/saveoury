package com.munawirfikri.saveoury.ui.main.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.munawirfikri.saveoury.R
import com.munawirfikri.saveoury.data.source.local.SharedPreference
import com.munawirfikri.saveoury.databinding.FragmentProfileBinding
import com.munawirfikri.saveoury.ui.login.LoginActivity

class ProfileFragment : Fragment(), View.OnClickListener {

    private val profileViewModel: ProfileViewModel by viewModels()
    private var profileAdapter: ProfileAdapter? = null
    private lateinit var sharedPref : SharedPreference
    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        sharedPref = SharedPreference(this.requireContext())
        binding.btnKeluar.setOnClickListener(this)
        val name = sharedPref.getValueString("name")
        val email = sharedPref.getValueString("email")
        val imageProfile = sharedPref.getValueString("photo")
        binding.tvNama.text = name.toString()
        binding.tvEmail.text = email.toString()
        Glide.with(this)
            .load(imageProfile)
            .error(R.drawable.ic_broken_image)
            .centerCrop()
            .placeholder(R.drawable.ic_person)
            .into(binding.profile.imgProfile)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (activity!=null){
            profileAdapter = ProfileAdapter()
            val userId = sharedPref.getValueString("id_user").toString()
            Log.d("userid", userId)
            val city = sharedPref.getValueString("city").toString()
            profileViewModel.getFoodPostByUserId(city, userId)
            profileViewModel.foodPost.observe(viewLifecycleOwner, {
                profileAdapter?.setData(it)
                profileAdapter?.notifyDataSetChanged()
                binding.tvKosong.visibility = if (it.isNotEmpty()) View.GONE else View.VISIBLE
            })
            profileViewModel.isLoading.observe(viewLifecycleOwner, {
                binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
            })
            with(binding.rvMyFood){
                this.layoutManager = LinearLayoutManager(context)
                this.setHasFixedSize(true)
                this.adapter = profileAdapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_keluar -> {
                sharedPref.clearSharedPreference()
                val intent = Intent(this.requireContext(), LoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }
}
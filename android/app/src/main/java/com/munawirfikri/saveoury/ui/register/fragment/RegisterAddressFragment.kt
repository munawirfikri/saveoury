package com.munawirfikri.saveoury.ui.register.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.munawirfikri.saveoury.R
import com.munawirfikri.saveoury.databinding.FragmentRegisterAddressBinding
import com.munawirfikri.saveoury.ui.main.MainActivity


class RegisterAddressFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentRegisterAddressBinding

    companion object {
        var EXTRA_EMAIL = "extra_email"
        var EXTRA_PASSWORD = "extra_password"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRegisterAddressBinding.inflate(layoutInflater, container, false)

        if (arguments != null) {
            val data = ArrayList<String>()
            val email = arguments?.getString(EXTRA_EMAIL).toString()
            val password = arguments?.getString(EXTRA_PASSWORD).toString()
            val name = binding.etNamaLengkap.text.toString()
            val phoneNumber = binding.etNomorHp.text.toString()
            val address = binding.etAlamatRumah.text.toString()
            val city = binding.etKota.text.toString()

            // PANGGIL API DISINI (POST)

            // END OF API

            binding.btnDaftarSekarang.setOnClickListener(this)
        }

        return binding.root
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_daftar_sekarang -> {
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }
    }

}
package com.munawirfikri.saveoury.ui.register.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.munawirfikri.saveoury.R
import com.munawirfikri.saveoury.databinding.FragmentRegisterAddressBinding
import com.munawirfikri.saveoury.ui.main.MainActivity
import com.munawirfikri.saveoury.ui.register.RegisterViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch


@ExperimentalCoroutinesApi
@FlowPreview
class RegisterAddressFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentRegisterAddressBinding


    private val registerViewModel: RegisterViewModel by viewModels()

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

        binding.edKota.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                lifecycleScope.launch {
                    registerViewModel.queryChannel.send(s.toString())
                }
            }
        })

        registerViewModel.searchResult.observe(viewLifecycleOwner, { placesItem ->
            val placesName = arrayListOf<String?>()
            placesItem.map {
                placesName.add(it.placeName)
            }
            val adapter = ArrayAdapter(requireActivity(), android.R.layout.select_dialog_item, placesName)
            adapter.notifyDataSetChanged()
            binding.edKota.setAdapter(adapter)
        })

        if (arguments != null) {
//            val data = ArrayList<String>()
//            val email = arguments?.getString(EXTRA_EMAIL).toString()
//            val password = arguments?.getString(EXTRA_PASSWORD).toString()
//            val name = binding.etNamaLengkap.text.toString()
//            val phoneNumber = binding.etNomorHp.text.toString()
//            val address = binding.etAlamatRumah.text.toString()
//            val city = binding.edKota.text.toString()

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
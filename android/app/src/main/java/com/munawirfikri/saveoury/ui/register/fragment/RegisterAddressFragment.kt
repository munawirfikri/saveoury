package com.munawirfikri.saveoury.ui.register.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.munawirfikri.saveoury.R
import com.munawirfikri.saveoury.data.source.local.SharedPreference
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
    private lateinit var sharedPref: SharedPreference

    companion object {
        var EXTRA_EMAIL = "extra_email"
        var EXTRA_PASSWORD = "extra_password"
        var EXTRA_PHOTO = "extra_photo"
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRegisterAddressBinding.inflate(layoutInflater, container, false)
        sharedPref = SharedPreference(this.requireContext())
        val photo = arguments?.getString(EXTRA_PHOTO)
        Log.d("hasil photo", photo.toString())

        binding.etKota.addTextChangedListener(object : TextWatcher {
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
            binding.etKota.setAdapter(adapter)
        })

        if (arguments != null) {
            binding.btnDaftarSekarang.setOnClickListener(this)
            binding.ivBack.setOnClickListener(this)
        }

        return binding.root
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.iv_back -> parentFragmentManager.popBackStack()
            R.id.btn_daftar_sekarang -> {
                val email = arguments?.getString(EXTRA_EMAIL).toString()
                val password = arguments?.getString(EXTRA_PASSWORD).toString()
                val name = binding.etNamaLengkap.text.toString()
                val phoneNumber = binding.etNomorHp.text.toString()
                val address = binding.etAlamatRumah.text.toString()
                val city = binding.etKota.text.toString().uppercase()

                if (name.isNotEmpty() && phoneNumber.isNotEmpty() && address.isNotEmpty() && city.isNotEmpty()){
                    registerViewModel.registerUser(name, email, password, phoneNumber,address,city)
                    registerViewModel.user.observe(viewLifecycleOwner, {user ->
                        if (user != null){
                            sharedPref.run {
                                save("id_user", user.user.id.toString())
                                save("token", user.tokenType.toString() + " " + user.accessToken.toString())
                                save("name", user.user.name.toString())
                                save("email", user.user.email.toString())
                                save("address", user.user.address.toString())
                                save("phoneNumber", user.user.phoneNumber.toString())
                                save("city", user.user.city.toString())
                            }
                            val photo = arguments?.getString(EXTRA_PHOTO)
                            registerViewModel.uploadPhotoUser(user.tokenType.toString() + " " + user.accessToken.toString(), photo.toString())
                            sharedPref.save("photo", user.user.profilePhotoUrl.toString())
                            val intent = Intent(context, MainActivity::class.java)
                            startActivity(intent)
                            activity?.finish()
                        }
                    })
                    registerViewModel.itExist.observe(viewLifecycleOwner, {
                        if(it == false){
                            Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT).show()
                        }
                    })
                    registerViewModel.isLoading.observe(this, {
                        binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
                    })
                }
            }
        }
    }

}
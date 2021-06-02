package com.munawirfikri.saveoury.ui.main.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import com.munawirfikri.saveoury.R
import com.munawirfikri.saveoury.data.source.local.SharedPreference
import com.munawirfikri.saveoury.databinding.FragmentProfileBinding
import com.munawirfikri.saveoury.ui.login.LoginActivity
import java.io.File

class ProfileFragment : Fragment(), View.OnClickListener {

    private val profileViewModel: ProfileViewModel by viewModels()
    private var profileAdapter: ProfileAdapter? = null
    private lateinit var sharedPref : SharedPreference
    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object {
        private const val PROFILE_IMAGE_REQ_CODE = 101
    }

    private var mProfileUri: Uri? = null


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
        binding.tvEditProfil.setOnClickListener(this)
        mProfileUri = imageProfile?.toUri()
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
            binding.editProfile.profile.imgProfile.setOnClickListener {
                pickProfileImage(it)
            }
            binding.editProfile.btnSubmit.setOnClickListener(this)
            with(binding.rvMyFood){
                this.layoutManager = LinearLayoutManager(context)
                this.setHasFixedSize(true)
                this.adapter = profileAdapter
            }
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun pickProfileImage(view: View) {
        ImagePicker.with(this)
            // Crop Square image
            .cropSquare()
            .setImageProviderInterceptor { imageProvider -> // Intercept ImageProvider
                Log.d("ImagePicker", "Selected ImageProvider: " + imageProvider.name)
            }
            .setDismissListener {
                Log.d("ImagePicker", "Dialog Dismiss")
            }
            // Image resolution will be less than 512 x 512
            .maxResultSize(200, 200)
            .saveDir(File(requireActivity().cacheDir, "user"))
            .start(PROFILE_IMAGE_REQ_CODE)
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                // Uri object will not be null for RESULT_OK
                val uri: Uri = data?.data!!
                when (requestCode) {
                    PROFILE_IMAGE_REQ_CODE -> {
                        mProfileUri = uri
                        binding.editProfile.profile.imgProfile.setImageURI(mProfileUri)
                    }
                }
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
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
            R.id.tv_edit_profil -> {
                binding.editProfile.root.visibility = View.VISIBLE
                binding.editProfile.btnCancel.setOnClickListener {
                    binding.editProfile.root.visibility = View.GONE
                }
                val imageProfile = sharedPref.getValueString("photo")
                Glide.with(this)
                    .load(imageProfile)
                    .error(R.drawable.ic_broken_image)
                    .centerCrop()
                    .placeholder(R.drawable.ic_person)
                    .into(binding.editProfile.profile.imgProfile)
                binding.editProfile.etNamaLengkap.hint = sharedPref.getValueString("name")
                binding.editProfile.etEmail.hint = sharedPref.getValueString("email")
                binding.editProfile.etAlamatRumah.hint = sharedPref.getValueString("address")
                binding.editProfile.etNomorHp.hint = sharedPref.getValueString("phoneNumber")
                binding.editProfile.etKota.hint = sharedPref.getValueString("city")
            }
            R.id.btn_submit -> {
                val nama = binding.editProfile.etNamaLengkap.text
                val email = binding.editProfile.etEmail.text
                val alamatRumah = binding.editProfile.etAlamatRumah.text
                val nomorHp = binding.editProfile.etNomorHp.text
                val kota = binding.editProfile.etKota.text
                val token = sharedPref.getValueString("token")

                if(nama.isNotEmpty() && email.isNotEmpty() && alamatRumah.isNotEmpty() && nomorHp.isNotEmpty() && kota.isNotEmpty()){
                    profileViewModel.updateUser(token.toString(), email.toString(), nama.toString(), alamatRumah.toString(), nomorHp.toString(), kota.toString().uppercase())
                    val photo = mProfileUri?.lastPathSegment.toString()
                    profileViewModel.uploadPhotoUser(token.toString(), photo)
                    Snackbar.make(binding.root, "Data profil telah diperbarui", Snackbar.LENGTH_SHORT).show()
                    sharedPref.clearSharedPreference()
                    val intent = Intent(this.requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }else{
                    Snackbar.make(binding.root, "Silahkan diisikan semua terlebih dahulu", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}
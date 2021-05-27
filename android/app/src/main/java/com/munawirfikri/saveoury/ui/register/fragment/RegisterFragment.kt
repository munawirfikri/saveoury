@file:Suppress("DEPRECATION")

package com.munawirfikri.saveoury.ui.register.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.github.dhaval2404.imagepicker.ImagePicker
import com.munawirfikri.saveoury.R
import com.munawirfikri.saveoury.databinding.ContentProfileBinding
import com.munawirfikri.saveoury.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var profileBinding: ContentProfileBinding

    companion object {
        private const val PROFILE_IMAGE_REQ_CODE = 101
    }

    private var mProfileUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        profileBinding = binding.contentProfile
        profileBinding.imgProfile.setOnClickListener(this)
        binding.btnLanjut.setOnClickListener(this)
        return binding.root
    }

    @Suppress("UNUSED_PARAMETER")
    fun pickProfileImage(view: View) {
        ImagePicker.with(this)
            // Crop Square image
            .galleryOnly()
            .cropSquare()
            .setImageProviderInterceptor { imageProvider -> // Intercept ImageProvider
                Log.d("ImagePicker", "Selected ImageProvider: " + imageProvider.name)
            }
            .setDismissListener {
                Log.d("ImagePicker", "Dialog Dismiss")
            }
            // Image resolution will be less than 512 x 512
            .maxResultSize(200, 200)
            .start(PROFILE_IMAGE_REQ_CODE)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                // Uri object will not be null for RESULT_OK
                val uri: Uri = data?.data!!
                when (requestCode) {
                    PROFILE_IMAGE_REQ_CODE -> {
                        mProfileUri = uri
                        profileBinding.imgProfile.setImageURI(mProfileUri)
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

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.imgProfile -> pickProfileImage(v)
            R.id.btn_lanjut -> {

                val mBundle = Bundle()

                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()

                mBundle.putString(RegisterAddressFragment.EXTRA_EMAIL, email)
                mBundle.putString(RegisterAddressFragment.EXTRA_PASSWORD, password)

                val mAddressFragment = RegisterAddressFragment()
                val mFragmentManager = parentFragmentManager

                mAddressFragment.arguments = mBundle

                mFragmentManager.beginTransaction().apply {
                    replace(R.id.register_frame_container, mAddressFragment, RegisterAddressFragment::class.java.simpleName)
                    addToBackStack(null)
                    commit()
                }
            }
        }
    }
}
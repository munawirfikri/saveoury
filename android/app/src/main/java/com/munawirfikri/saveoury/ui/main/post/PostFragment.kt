package com.munawirfikri.saveoury.ui.main.post

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.util.IntentUtils
import com.google.android.material.snackbar.Snackbar
import com.munawirfikri.saveoury.R
import com.munawirfikri.saveoury.data.source.local.SharedPreference
import com.munawirfikri.saveoury.databinding.FragmentPostBinding
import java.io.File

class PostFragment : Fragment(), View.OnClickListener {

    private val postViewModel: PostViewModel by viewModels()
    private var _binding: FragmentPostBinding? = null
    private lateinit var sharedPref: SharedPreference
    private val binding get() = _binding!!

    companion object {
        private const val CAMERA_IMAGE_REQ_CODE = 103
    }
    private var mCameraUri: Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostBinding.inflate(inflater, container, false)
        sharedPref = SharedPreference(this.requireContext())
        binding.imageContainer.imgCamera.setOnClickListener(this)
        binding.imageContainer.fabAddCameraPhoto.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (activity!=null){
            binding.btnSubmit.isEnabled = false
            postViewModel.isValidImage.observe(viewLifecycleOwner, {
                if (it != 1){
                    binding.btnSubmit.isEnabled = false
                    Snackbar.make(binding.root, "Foto tidak valid, silahkan unggah foto makanan!", Snackbar.LENGTH_SHORT).show()
                }else{
                    binding.btnSubmit.isEnabled = true
                }
            })
            postViewModel.isLoading.observe(viewLifecycleOwner, {
                binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
            })
        }
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                // Uri object will not be null for RESULT_OK
                val uri: Uri = data?.data!!
                when (requestCode) {
                    CAMERA_IMAGE_REQ_CODE -> {
                        mCameraUri = uri
                        binding.imageContainer.imgCamera.setImageURI(uri)
                        val image = mCameraUri?.lastPathSegment.toString()
                        postViewModel.predictImage(image)
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

    private fun showImage(view: View) {
        val uri = when (view) {
            binding.imageContainer.imgCamera -> mCameraUri
            else -> null
        }
        uri?.let {
            startActivity(IntentUtils.getUriViewIntent(this.requireContext(), uri))
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun pickImage(view: View) {
        ImagePicker.with(this)
            // Crop Square image
            .cropSquare()
            .setImageProviderInterceptor { imageProvider -> // Intercept ImageProvider
                Log.d("ImagePicker", "Selected ImageProvider: " + imageProvider.name)
            }
            .setDismissListener {
                Log.d("ImagePicker", "Dialog Dismiss")
            }
            .saveDir(File(requireActivity().cacheDir, "foodpost"))
            .start(CAMERA_IMAGE_REQ_CODE)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.imgCamera -> showImage(v)
            R.id.fab_add_camera_photo -> pickImage(v)
            R.id.btn_submit -> {
                val authorization = sharedPref.getValueString("token").toString()
                val image = mCameraUri?.lastPathSegment.toString()
                val foodName = binding.etFoodName.text.toString()
                val foodDesc = binding.etFoodDesc.text.toString()
                val foodCategory = if(binding.radioBerat.isChecked) binding.radioBerat.text else if (binding.radioRingan.isChecked) binding.radioRingan.text else null
                val location = sharedPref.getValueString("city").toString()
                if(authorization.isNotEmpty() && image.isNotEmpty() && foodName.isNotEmpty() && foodDesc.isNotEmpty() && location.isNotEmpty()){
                    binding.etFoodName.text?.clear()
                    binding.etFoodDesc.text?.clear()
                    binding.radioCategory.clearCheck()
                    binding.imageContainer.imgCamera.setImageResource(R.color.grey)
                    postViewModel.addFoodPost(
                        authorization, image,
                        foodName, foodDesc,
                        foodCategory.toString(), location, "1", "1")
                    Snackbar.make(binding.root, "Postingan berhasil ditambahkan", Snackbar.LENGTH_SHORT).show()
                }else{
                    Snackbar.make(binding.root, "Silahkan diisi semua terlebih dahulu", Snackbar.LENGTH_SHORT).show()
                }

            }
        }
    }
}
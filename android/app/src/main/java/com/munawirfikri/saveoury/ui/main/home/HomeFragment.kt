package com.munawirfikri.saveoury.ui.main.home

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.munawirfikri.saveoury.R
import com.munawirfikri.saveoury.data.source.local.SharedPreference
import com.munawirfikri.saveoury.databinding.FragmentHomeBinding

class HomeFragment : Fragment(), View.OnClickListener {

    private val homeViewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    private lateinit var sharedPref: SharedPreference



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(activity != null){
            sharedPref = SharedPreference(this.requireContext())
            val city = sharedPref.getValueString("city")
            val location = sharedPref.getValueString("address")
            if (location!=null){
                binding.tvLocation.text = location.toString()
                binding.tvLocation.setOnClickListener(this)
            }
            Log.d("Data User", sharedPref.getValueString("photo").toString())
            homeViewModel.showFoodPost(city.toString())
        }
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showDialog(alamat: String) {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setMessage("Alamat kamu: " + alamat)
        dialogBuilder.setPositiveButton("Oke!",
            { dialog, whichButton -> })
        val b = dialogBuilder.create()
        b.show()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_location -> {
                val alamat = sharedPref.getValueString("address")
                showDialog(alamat.toString())
            }
        }
    }

}
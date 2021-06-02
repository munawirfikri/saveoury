package com.munawirfikri.saveoury.ui.main.home

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.munawirfikri.saveoury.R
import com.munawirfikri.saveoury.data.source.local.SharedPreference
import com.munawirfikri.saveoury.databinding.FragmentHomeBinding

class HomeFragment : Fragment(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private val homeViewModel: HomeViewModel by viewModels()
    private var homeAdapter: HomeAdapter? = null
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    private lateinit var sharedPref: SharedPreference



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(activity != null){
            homeAdapter = HomeAdapter()
            sharedPref = SharedPreference(this.requireContext())
            val city = sharedPref.getValueString("city")
            val location = sharedPref.getValueString("address")
            if (location!=null){
                binding.tvLocation.text = location.toString()
                binding.tvLocation.setOnClickListener(this)
                binding.tvKetuk.setOnClickListener(this)
            }

            binding.swipeContainer.setOnRefreshListener(this)

            homeViewModel.showFoodPost(city.toString())

            homeViewModel.foodPost.observe(viewLifecycleOwner, {
                homeAdapter?.setData(it)
                homeAdapter?.notifyDataSetChanged()
                binding.viewEmpty.root.visibility = if (it.isNotEmpty()) View.GONE else View.VISIBLE
            })

            homeViewModel.isLoading.observe(viewLifecycleOwner, {
                binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
            })

            with(binding.rvHome){
                this.layoutManager = LinearLayoutManager(context)
                this.setHasFixedSize(true)
                this.adapter = homeAdapter
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showDialog(alamat: String) {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setMessage(alamat)
        dialogBuilder.setPositiveButton("Oke!") { _, _ -> }
        val b = dialogBuilder.create()
        b.show()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_location -> {
                val alamat = sharedPref.getValueString("address")
                showDialog(alamat.toString())
            }
            R.id.tv_ketuk -> {
                val alamat = sharedPref.getValueString("address")
                showDialog(alamat.toString())
            }
        }
    }

    override fun onRefresh() {
        val city = sharedPref.getValueString("city")
        homeViewModel.showFoodPost(city.toString())
        homeViewModel.foodPost.observe(viewLifecycleOwner, {
            homeAdapter?.setData(it)
            homeAdapter?.notifyDataSetChanged()
        })
        homeViewModel.isLoading.observe(viewLifecycleOwner, {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
            binding.rvHome.visibility = if (it) View.GONE else View.VISIBLE
        })
        Handler(Looper.getMainLooper()).postDelayed({
            binding.swipeContainer.isRefreshing = false
        }, 1000)
    }

}
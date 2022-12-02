package com.example.byeprivacy.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.core.view.size
import androidx.navigation.Navigation
import com.example.byeprivacy.R
import com.example.byeprivacy.data.api.helpers.PreferenceData
import com.example.byeprivacy.databinding.FragmentBarsBinding
import com.example.byeprivacy.ui.viewmodels.BarsViewModel
import com.example.byeprivacy.ui.viewmodels.LoginViewModel
import com.example.byeprivacy.utils.AppLocation
import com.example.byeprivacy.utils.Injection
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class BarsFragment : Fragment() {
    private lateinit var binding: FragmentBarsBinding
    private lateinit var viewModel: BarsViewModel
    private lateinit var location: AppLocation
    //TODO sorting
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, Injection.provideViewModelFactory(requireContext())).get(
            BarsViewModel::class.java)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentBarsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("bars on binding","bars on binding")
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            model = viewModel
        }.also { bind ->
            bind.swiperefresh.setOnRefreshListener {
                loadData(0)
            }
            bind.sortdist.setOnClickListener {
                loadData(1)
                Log.d("numberofbars",binding.recyclerviewbars.top.toString())

            }
            bind.ludia.setOnClickListener {
                loadData(-3)
            }
            bind.aZ.setOnClickListener {
                loadData(2)
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            binding.swiperefresh.isRefreshing = it
        }
        viewModel.message.observe(viewLifecycleOwner){
            if (PreferenceData.getInstance().getUserItem(requireContext()) == null) {
                Navigation.findNavController(requireView()).navigate(R.id.action_global_loginFragment)
            }
        }
    }
    private val gadgetQ = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private fun approveForegroundAndBackgroundLocation(): Boolean {
        val foregroundLocationApproved = (
                PermissionChecker.PERMISSION_GRANTED == activity?.let {
                    ActivityCompat.checkSelfPermission(
                        it, Manifest.permission.ACCESS_FINE_LOCATION
                    )
                })
        val backgroundPermissionApproved =
            if (gadgetQ) {
                PermissionChecker.PERMISSION_GRANTED == activity?.let {
                    ActivityCompat.checkSelfPermission(
                        it, Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    )
                }
            } else {
                true
            }
        return foregroundLocationApproved && backgroundPermissionApproved
    }

    @SuppressLint("MissingPermission")
    private fun loadData(sort_val: Int) {
        Log.d("loadData","loading bars")
        if (approveForegroundAndBackgroundLocation()) {
            viewModel.loading.postValue(true)
            fusedLocationClient.getCurrentLocation(
                CurrentLocationRequest.Builder().setDurationMillis(30000)
                    .setMaxUpdateAgeMillis(60000).build(), null
            ).addOnSuccessListener {
                it?.let {
                    Log.d("loadData",it.latitude.toString())

                    viewModel.appLocation.postValue(AppLocation(it.latitude, it.longitude))
                    viewModel.refreshData(sort_val,AppLocation(it.latitude, it.longitude))

                } ?: viewModel.loading.postValue(false)
            }
        }
    }
}
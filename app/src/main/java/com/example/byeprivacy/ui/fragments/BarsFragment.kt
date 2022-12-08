package com.example.byeprivacy.ui.fragments

import android.Manifest
import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.byeprivacy.data.api.helpers.PreferenceData
import com.example.byeprivacy.databinding.FragmentBarsBinding
import com.example.byeprivacy.ui.viewmodels.BarsViewModel
import com.example.byeprivacy.utils.AppLocation
import com.example.byeprivacy.utils.Injection
import com.example.byeprivacy.utils.infoGPSandNetwork
import com.example.byeprivacy.utils.permissionDialogFun
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class BarsFragment : Fragment() {
    private lateinit var binding: FragmentBarsBinding
    private lateinit var viewModel: BarsViewModel
    private val gadgetQ = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q
    private lateinit var fusedLocationClient: FusedLocationProviderClient


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

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val x = PreferenceData.getInstance().getUserItem(requireContext())
        if ((x?.uid ?: "").isBlank()) {
            Navigation.findNavController(view).navigate(com.example.byeprivacy.R.id.action_global_loginFragment)
            return
        }

        Log.d("q",gadgetQ.toString())
        if (!approveForegroundAndBackgroundLocation()){
            if (gadgetQ){
                permissionDialogFun(requireContext(),requireActivity(),requireView())
            }
        }
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            model = viewModel
        }.also { bind ->
            bind.swiperefresh.setOnRefreshListener {
                loadData(0)
            }
            bind.headerTitle.setOnClickListener{
                if (bind.sortCard.visibility == View.VISIBLE){
                    bind.sortCard.visibility = View.GONE

                }else{
                    bind.sortCard.visibility = View.VISIBLE

                    bind.sortdist.setOnClickListener {
                        bind.sortCard.visibility = View.GONE
                        loadData(-1)
                    }
                    bind.sortdistDown.setOnClickListener {
                        bind.sortCard.visibility = View.GONE
                        loadData(1)
                    }
                    bind.aZ.setOnClickListener {
                        bind.sortCard.visibility = View.GONE
                        loadData(-2)
                    }
                    bind.zA.setOnClickListener {
                        bind.sortCard.visibility = View.GONE
                        loadData(2)
                    }
                    bind.ludiaUp.setOnClickListener {
                        bind.sortCard.visibility = View.GONE
                        loadData(-3)
                    }
                    bind.ludiaDown.setOnClickListener {
                        bind.sortCard.visibility = View.GONE
                        loadData(3)
                    }
                }


            }

        }

        viewModel.loading.observe(viewLifecycleOwner) {
            binding.swiperefresh.isRefreshing = it
            Log.d("it",it.toString())
        }
        viewModel.message.observe(viewLifecycleOwner){
            if (PreferenceData.getInstance().getUserItem(requireContext()) == null) {
                Navigation.findNavController(requireView()).navigate(com.example.byeprivacy.R.id.action_global_loginFragment)
            }
        }
        infoGPSandNetwork(requireContext())

    }



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
        Log.d("loadData",approveForegroundAndBackgroundLocation().toString())
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
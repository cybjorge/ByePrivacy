package com.example.byeprivacy.ui.fragments


import android.Manifest
import android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.byeprivacy.CheckinBar.checkedBar
import com.example.byeprivacy.CheckinBar.checkedbool
import com.example.byeprivacy.R
import com.example.byeprivacy.data.db.models.BarApiItem
import com.example.byeprivacy.databinding.FragmentBarsWithLocationBinding
import com.example.byeprivacy.ui.viewmodels.BarsViewModel
import com.example.byeprivacy.ui.viewmodels.BarsWithLocationViewModel
import com.example.byeprivacy.ui.widgets.locationBars.InterfaceBarsCheckIn
import com.example.byeprivacy.ui.widgets.locationBars.RecyclerViewCheckIn
import com.example.byeprivacy.utils.AppLocation

import com.example.byeprivacy.utils.GeofenceBroadcastReceiver
import com.example.byeprivacy.utils.Injection
import com.google.android.gms.location.*

class BarsWithLocation : Fragment() {
    private lateinit var binding: FragmentBarsWithLocationBinding
    private lateinit var viewModel: BarsWithLocationViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geofencingClient: GeofencingClient

    private val gadgetQ = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(ACCESS_BACKGROUND_LOCATION, false) -> {
                // Precise location access granted.
            }
            else -> {
                viewModel.show("Background location access denied.")
                // No location access granted.
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, Injection.provideViewModelFactory(requireContext())).get(
            BarsWithLocationViewModel::class.java)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        geofencingClient = LocationServices.getGeofencingClient(requireActivity())
    }


    private fun approveForegroundAndBackgroundLocation(): Boolean {
        val foregroundLocationApproved = (
                PERMISSION_GRANTED == activity?.let {
                    ActivityCompat.checkSelfPermission(
                        it, ACCESS_FINE_LOCATION
                    )
                })
        val backgroundPermissionApproved =
            if (gadgetQ) {
                PERMISSION_GRANTED == activity?.let {
                    ActivityCompat.checkSelfPermission(
                        it, ACCESS_BACKGROUND_LOCATION
                    )
                }
            } else {
                true
            }
        return foregroundLocationApproved && backgroundPermissionApproved
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentBarsWithLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            model = viewModel
        }.also { bind->
            bind.swiperefresh.setOnRefreshListener {
                loadData()
            }
            if (viewModel.checkinBar.value?.name.isNullOrBlank()){
                bind.headerTitle.text="Currently nowhere"
            }
            bind.animationViewPin.setOnClickListener{
                if (approveForegroundAndBackgroundLocation()){
                    viewModel.checkMeIn()
                    bind.animationViewPin.visibility = View.VISIBLE
                    bind.animationCheck.visibility = View.GONE
                    Log.d("checkbar", checkedBar)
                    if (checkedbool){
                        bind.headerTitle.text="Currently in "+ checkedBar
                    }
                }else{
                    if (gadgetQ){
                        permissionDialog()
                    }
                }
            }
            if (checkedbool==false){
                bind.headerTitle.text="Currently nowhere"
            }
            bind.nearbyBars.events = object : InterfaceBarsCheckIn{
                override fun onClickCheckIn(bar: BarApiItem) {
                    bind.animationViewPin.visibility = View.VISIBLE
                    bind.animationCheck.visibility = View.GONE
                    viewModel.checkinBar.postValue(bar)
                }
                //TODO checkout and check if im checked in

            }
            viewModel.loading.observe(viewLifecycleOwner) {
                binding.swiperefresh.isRefreshing = it
            }
            viewModel.checkedIn.observe(viewLifecycleOwner) {
                it?.getContentIfNotHandled()?.let {
                    if (it) {
                        viewModel.show("Successfully checked in.")
                        bind.animationViewPin.visibility = View.GONE
                        bind.animationCheck.visibility = View.VISIBLE
                        bind.animationCheck.playAnimation()
                        viewModel.appLocation.value?.let {
                            createFence(it.lat, it.lon)
                        }
                    }
                }
            }
            if (approveForegroundAndBackgroundLocation()) {
                loadData()
            } else {
                Navigation.findNavController(requireView()).navigate(R.id.action_global_barsFragment)
            }
        }
    }
    @SuppressLint("MissingPermission")
    private fun loadData() {
        Log.d("loadData","loading bars")
        if (approveForegroundAndBackgroundLocation()) {
            viewModel.loading.postValue(true)
            fusedLocationClient.getCurrentLocation(
                CurrentLocationRequest.Builder().setDurationMillis(30000)
                    .setMaxUpdateAgeMillis(60000).build(), null
            ).addOnSuccessListener {
                it?.let {
                    viewModel.appLocation.postValue(AppLocation(it.latitude, it.longitude))
                } ?: viewModel.loading.postValue(false)
            }
        }
    }

    @SuppressLint("MissingPermission", "UnspecifiedImmutableFlag")
    private fun createFence(lat: Double, lon: Double) {
        if (!approveForegroundAndBackgroundLocation()) {
            viewModel.show("Geofence failed, permissions not granted.")
        }
        val geofenceIntent = PendingIntent.getBroadcast(
            requireContext(), 0,
            Intent(requireContext(), GeofenceBroadcastReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val request = GeofencingRequest.Builder().apply {
            addGeofence(
                Geofence.Builder()
                    .setRequestId("mygeofence")
                    .setCircularRegion(lat, lon, 300F)
                    .setExpirationDuration(1000L * 60 * 60 * 24)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build()
            )
        }.build()

        geofencingClient.addGeofences(request, geofenceIntent).run {
            addOnSuccessListener {
                viewModel.show("Geofence created.")
            }
            addOnFailureListener {
                viewModel.show("Geofence failed to create.") //permission is not granted for All times.
                it.printStackTrace()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun permissionDialog() {
        val alertDialog: AlertDialog = requireActivity().let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle("Background location needed")
                setMessage("Allow background location (All times) for detecting when you leave bar.")
                setPositiveButton("OK",
                    DialogInterface.OnClickListener { dialog, id ->
                        locationPermissionRequest.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION
                            )
                        )
                    })
                setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                    })
            }
            // Create the AlertDialog
            builder.create()
        }
        alertDialog.show()
    }


}
package com.example.byeprivacy.utils

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation

fun infoGPSandNetwork(context: Context){
    val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    var gps_enabled = false
    var network_enabled = false

    try {
        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
    } catch (ex: Exception) {
    }

    try {
        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    } catch (ex: Exception) {
    }

    if (!gps_enabled && !network_enabled) {
        // notify user
        AlertDialog.Builder(context)
            .setMessage(com.example.byeprivacy.R.string.gps_network_not_enabled)
            .setPositiveButton(
                com.example.byeprivacy.R.string.open_location_settings,
                DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                    context.startActivity(
                        Intent(
                            Settings.ACTION_LOCATION_SOURCE_SETTINGS
                        )
                    )
                })
            .setNegativeButton(com.example.byeprivacy.R.string.Cancel, null)
            .show()
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
fun permissionDialogFun(context: Context,activity: FragmentActivity,view: View) {
    val alertDialog: AlertDialog = activity.let {
        val builder = AlertDialog.Builder(it)
        builder.apply {
            setTitle("Background location needed")
            setMessage("Allow background location (All times) for detecting when you leave bar.")
            setPositiveButton("OK",
                DialogInterface.OnClickListener { dialog, id ->
                    context.startActivity( Intent(Settings.ACTION_APPLICATION_SETTINGS))
                }

            )
            setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, id ->
                    Navigation.findNavController(view).navigate(com.example.byeprivacy.R.id.action_global_barsFragment)
                })
        }
        // Create the AlertDialog
        builder.create()
    }
    alertDialog.show()
}
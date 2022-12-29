package com.inflexionlabs.ringtoner.presentation.screens.components

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.provider.Settings

object Alerts {

    fun permissionAlert(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("Permission needed")
            .setMessage("This permission is required to set ringtone, sms tone and contact tone," +
                    " you will be redirected to the settings panel please enable the permission")
            .setPositiveButton("ALLOW") { _: DialogInterface?, _: Int ->
                val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                intent.data = Uri.parse("package:" + context.packageName)
                context.startActivity(intent)
            }
            .setNegativeButton(
                "CANCEL"
            ) { dialogInterface: DialogInterface, _: Int -> dialogInterface.dismiss() }
            .create()
            .show()
    }

    fun networkAlert(context: Context) {

        AlertDialog.Builder(context)
            .setTitle("No internet connection")
            .setMessage("Check your internet connection and restart the application")
            .setPositiveButton("ALRIGHT"
            ) { dialogInterface : DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }.setCancelable(false).show()
    }

    fun permissionRationaleAlert(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("Permission needed!")
            .setMessage("As app will no longer be able to ask permissions please allow required permissions in app settings, to use the functionality of the app")
            .setPositiveButton("ALRIGHT") { _: DialogInterface?, _: Int -> }
            .setNegativeButton(
                "NO"
            ) { dialogInterface: DialogInterface, _: Int -> dialogInterface.dismiss() }
            .create()
            .show()
    }
}
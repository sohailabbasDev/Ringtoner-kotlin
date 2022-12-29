package com.inflexionlabs.ringtoner.presentation.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.inflexionlabs.ringtoner.BuildConfig
import com.inflexionlabs.ringtoner.R
import com.inflexionlabs.ringtoner.operations.RingtonePlayer
import com.inflexionlabs.ringtoner.presentation.navigation.Routes
import com.inflexionlabs.ringtoner.presentation.screens.components.animations.AboutPageAnim

@Composable
fun AboutScreen(navHostController: NavHostController){

    try {
        if (RingtonePlayer.isPLaying()){
            RingtonePlayer.stopPlaying()
        }
    }catch (e : Exception){
        e.stackTrace
    }

    val context = LocalContext.current
    val shareMessage = "\n" +
            "Hey i found some amazing ringtones, with vast " +
            "categories such as Bollywood, Marimba, Gaming, BGM, NCS and more in ${context.getString(R.string.app_name)} " +
            "download the app right now! \n" +
            "Click the link - https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID} \n"


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.size(16.dp))
        Text(modifier = Modifier
            .padding(start = 12.dp)
            .clickable { navHostController.navigate(Routes.Details.route) },
            text = "About",
            color = MaterialTheme.colors.primary,
            fontSize = 34.sp)
        Spacer(modifier = Modifier.size(24.dp))
        Text(modifier = Modifier
            .padding(start = 12.dp)
            .clickable {
                try {
                    context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=${context.applicationInfo.packageName}")
                        ))
                } catch (e: Exception) {
                    e.stackTrace
                    context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=${context.applicationInfo.packageName}")
                        ))
                }
            },
            text = "Rate us",
            color = MaterialTheme.colors.primary,
            fontSize = 34.sp)
        Spacer(modifier = Modifier.size(24.dp))
        Text(modifier = Modifier
            .padding(start = 12.dp)
            .clickable {
                try {
                    val shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.type = "text/plain"
                    shareIntent.putExtra(
                        Intent.EXTRA_SUBJECT,
                        context.applicationInfo.name
                    )
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                    context.startActivity(Intent.createChooser(shareIntent, "choose app to share"))
                } catch (e: Exception) {
                    e.stackTrace
                }
            },
            text = "Share app",
            color = MaterialTheme.colors.primary,
            fontSize = 34.sp)
        Spacer(modifier = Modifier.size(12.dp))
        AboutPageAnim()
    }
}
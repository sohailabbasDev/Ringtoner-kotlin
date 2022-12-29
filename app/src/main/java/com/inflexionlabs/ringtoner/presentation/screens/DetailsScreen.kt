package com.inflexionlabs.ringtoner.presentation.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun DetailsScreen(navHostController: NavHostController){

    val context = LocalContext.current

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(title = {}, navigationIcon = {
        IconButton(onClick = {navHostController.popBackStack()}) {
            Icon(imageVector = Icons.Default.ArrowBack,
                contentDescription = "back arrow",
                tint = MaterialTheme.colors.surface
            )
        } }, backgroundColor = MaterialTheme.colors.background, elevation = 0.dp
    ) })
    {it.calculateTopPadding()
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier.padding(start = 12.dp),
                text = "Ringtoner",
                fontSize = 45.sp,
                fontFamily = MaterialTheme.typography.body1.fontFamily,
                color = MaterialTheme.colors.primaryVariant
            )
            Text(
                modifier = Modifier.padding(18.dp),
                text = "Ringtoner is created to provide free ringtones which are customised and of various categories," +
                        " you can set a ringtone, alarm ringtone, notification ringtone and contact ringtone as well" +
                        " as download the preferred ringtone.\n",
                fontSize = 18.sp,
                letterSpacing = 0.02.sp,
                fontFamily = MaterialTheme.typography.body1.fontFamily,
                color = MaterialTheme.colors.primaryVariant
            )
            Text(
                modifier = Modifier.padding(start = 12.dp),
                text = "Developer",
                fontSize = 45.sp,
                fontFamily = MaterialTheme.typography.body1.fontFamily,
                color = Color.Red
            )
            Text(
                modifier = Modifier.padding(18.dp),
                text = "Ringtoner is developed and published by inflexion labs," +
                        " we develop android applications which are useful and fun to use.",
                fontSize = 16.sp,
                letterSpacing = 0.02.sp,
                fontFamily = MaterialTheme.typography.body1.fontFamily,
                color = MaterialTheme.colors.primaryVariant
            )
            Text(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .clickable {
                        try {
                            val browserIntent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://pages.flycricket.io/ringtoner/privacy.html")
                            )
                            context.startActivity(browserIntent)
                        } catch (e: Exception) {
                            e.stackTrace
                        }
                    },
                text = "Privacy policy",
                fontSize = 25.sp,
                fontFamily = MaterialTheme.typography.body1.fontFamily,
                color = MaterialTheme.colors.primaryVariant
            )
            Text(
                modifier = Modifier
                    .padding(12.dp)
                    .clickable {
                        try {
                            val intent = Intent(Intent.ACTION_SENDTO)
                            intent.data = Uri.parse("mailto:inflexionlabs@gmail.com") // only email apps should handle this
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            e.stackTrace
                        }
                    },
                text = "Have a feedback, tell us?",
                fontSize = 25.sp,
                fontFamily = MaterialTheme.typography.body1.fontFamily,
                color = MaterialTheme.colors.primaryVariant
            )
        }
    }
}
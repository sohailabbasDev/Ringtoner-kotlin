package com.inflexionlabs.ringtoner.presentation.screens

import android.Manifest
import android.media.RingtoneManager
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.inflexionlabs.ringtoner.MainUtil
import com.inflexionlabs.ringtoner.database.FavouritesViewModel
import com.inflexionlabs.ringtoner.database.model.Favourite
import com.inflexionlabs.ringtoner.database.state.FavouriteEvent
import com.inflexionlabs.ringtoner.firebase_database.model.Ringtone
import com.inflexionlabs.ringtoner.operations.Downloader
import com.inflexionlabs.ringtoner.operations.RingtonePlayer
import com.inflexionlabs.ringtoner.operations.connectivity.ConnectivityObserver
import com.inflexionlabs.ringtoner.operations.states.DownloadState
import com.inflexionlabs.ringtoner.operations.states.PlayingState
import com.inflexionlabs.ringtoner.operations.states.RingtoneSetState
import com.inflexionlabs.ringtoner.presentation.screens.components.Alerts
import com.inflexionlabs.ringtoner.presentation.screens.components.RingtoneOptions
import java.io.File

@ExperimentalPermissionsApi
@Composable
fun PlayScreen(navHostController: NavHostController,
               favouritesViewModel: FavouritesViewModel = hiltViewModel(),
               networkStatus: ConnectivityObserver.Status){

    try {
        if (RingtonePlayer.isPLaying()){
            RingtonePlayer.stopPlaying()
        }
    }catch (e : Exception){
        e.stackTrace
    }

    val ringtone = navHostController.previousBackStackEntry?.savedStateHandle?.get<Ringtone>("ringtone") ?: return

    var isFavourite by remember { mutableStateOf(FavouritesViewModel.isFavouriteList.contains(ringtone.name)) }

    val context = LocalContext.current

    val isLowerSDK = Build.VERSION.SDK_INT < Build.VERSION_CODES.P

    val contactPermission = rememberPermissionState(permission = Manifest.permission.WRITE_CONTACTS)

    val writeExternalPermission = rememberPermissionState(permission = Manifest.permission.WRITE_EXTERNAL_STORAGE)

    var contactPermissionAsk by remember{
        mutableStateOf(false)
    }

    var lowSDKPermission by remember{
        mutableStateOf(false)
    }

    var writePermissionAlert by remember{
        mutableStateOf(false)
    }

//    var adSeen by remember{
//        mutableStateOf(false)
//    }
//
//    var enabled by remember{
//        mutableStateOf(false)
//    }

//    var adLoading by remember{
//        mutableStateOf(false)
//    }

//    val scale by animateFloatAsState(
//        targetValue = if (enabled) .9f else 1f,
//        animationSpec = repeatable(
//            iterations = 5,
//            animation = tween(durationMillis = 50, easing = LinearEasing),
//            repeatMode = RepeatMode.Reverse
//        ),
//        finishedListener = {
//            enabled = false
//        }
//    )

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickContact()) {uri ->
        if (uri != null){
            if (Downloader.downloadingStatePub.value is DownloadState.Downloaded
                && Downloader.downloadingStatePub.value !is DownloadState.Started
            ) {
                Downloader.setContactRingtone(context, File(
                    Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_MUSIC
                    ), ringtone.name + ".mp3"
                ), uri)
                MainUtil.makeToast(context, "Contact tone set", Toast.LENGTH_SHORT)
            } else {
                Downloader
                    .downloadFromURL(ringtone)
                    .invokeOnCompletion {
                        Downloader.setContactRingtone(context, File(
                            Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_MUSIC
                            ), ringtone.name + ".mp3"
                        ), uri)
                    }
                MainUtil.makeToast(context, "Contact tone set", Toast.LENGTH_SHORT)
            }
        }
    }

    DisposableEffect(key1 = lowSDKPermission, effect = {
        writeExternalPermission.launchPermissionRequest()

        if (writeExternalPermission.status.shouldShowRationale && !writeExternalPermission.status.isGranted){
            Alerts.permissionRationaleAlert(context)
        }
        onDispose {  }
    })

    DisposableEffect(key1 = contactPermissionAsk, effect = {
        contactPermission.launchPermissionRequest()

        if (contactPermission.status.shouldShowRationale && !contactPermission.status.isGranted){
            Alerts.permissionRationaleAlert(context)
        }
        onDispose {  }
    })

    DisposableEffect(key1 = writePermissionAlert, effect = {
        if (!Settings.System.canWrite(context)) {
            Alerts.permissionAlert(context)
        }
        onDispose {  }
    })

    BackHandler(enabled = true) {
        Downloader.resetStates()
        navHostController.popBackStack()
    }

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(backgroundColor = MaterialTheme.colors.background, elevation = 0.dp){
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    navHostController.popBackStack()
                    Downloader.resetStates()
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack,
                        contentDescription = "back arrow",
                        tint = MaterialTheme.colors.surface
                    )
                }
                IconButton(onClick = {
                    isFavourite = if (isFavourite) {
                        favouritesViewModel.onEvent(FavouriteEvent.DeleteFavourite(ringtone.name.toString()))
                        FavouritesViewModel.isFavouriteList.remove(ringtone.name.toString())
                        false
                    }else{
                        favouritesViewModel.onEvent(FavouriteEvent.AddFavourite(Favourite(text = ringtone.name.toString(), url = ringtone.uri.toString())))
                        FavouritesViewModel.isFavouriteList.add(ringtone.name.toString())
                        true
                    }
                }
                ) {
                    Icon(imageVector = if (isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        tint = if (isFavourite) Color.Red else MaterialTheme.colors.surface,
                        contentDescription = "Favourite button")
                }
            }
        } })
    {it.calculateTopPadding()
        Column(modifier = Modifier.fillMaxSize()) {
            Text(modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
                text = ringtone.name.toString(),
                textAlign = TextAlign.Center,
                fontSize = 28.sp,
                fontFamily = MaterialTheme.typography.body1.fontFamily,
                color = MaterialTheme.colors.surface)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.2f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if(RingtonePlayer.playingStatePub.value == PlayingState.Preparing){return@IconButton}

                        if (networkStatus !is ConnectivityObserver.Status.Available){
                            Alerts.networkAlert(context)
                            return@IconButton
                        }

                        if (!RingtonePlayer.isPLaying()){
                            RingtonePlayer.playRingtone(ringtone.uri.toString(), ringtone.name.toString())
                        }else{
                            RingtonePlayer.stopPlaying()
                        }
                    }) {
                    when(RingtonePlayer.playingStatePub.value){
                        is PlayingState.Empty -> {
                            Icon(modifier = Modifier.fillMaxSize(),
                                imageVector = Icons.Default.PlayCircle,
                                contentDescription = "Play circle",
                                tint = MaterialTheme.colors.surface
                            )
                        }
                        is PlayingState.Preparing -> {
                            CircularProgressIndicator(color = MaterialTheme.colors.surface)
                        }
                        is PlayingState.Playing -> {
                            Icon(modifier = Modifier.fillMaxSize(),
                                imageVector = Icons.Default.StopCircle,
                                contentDescription = "Stop circle",
                                tint = MaterialTheme.colors.surface
                            )
                        }
                        is PlayingState.Stopped -> {
                            Icon(modifier = Modifier.fillMaxSize(),
                                imageVector = Icons.Default.PlayCircle,
                                contentDescription = "Play circle",
                                tint = MaterialTheme.colors.surface
                            )
                        }
                        is PlayingState.Failed -> {
                            Icon(modifier = Modifier.fillMaxSize(),
                                imageVector = Icons.Default.PlayCircle,
                                contentDescription = "Stop circle",
                                tint = MaterialTheme.colors.surface
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Column(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f), verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RingtoneOptions(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clickable(enabled = Downloader.ringtoneSetStatePub.value !is RingtoneSetState.Setting) {

                            if (networkStatus !is ConnectivityObserver.Status.Available) {
                                Alerts.networkAlert(context)
                                return@clickable
                            }

//                            if (!adSeen) {
//                                enabled = true
//                                return@clickable
//                            }

                            if (isLowerSDK) {
                                if (!writeExternalPermission.status.isGranted) {
                                    lowSDKPermission = true
                                    if (!Settings.System.canWrite(context)) {
                                        writePermissionAlert = true
                                    }
                                    return@clickable
                                }
                            } else {
                                if (!Settings.System.canWrite(context)) {
                                    writePermissionAlert = true
                                    return@clickable
                                }
                            }

                            if (Downloader.downloadingStatePub.value is DownloadState.Downloaded
                                && Downloader.downloadingStatePub.value !is DownloadState.Started
                            ) {
                                Downloader.setRingtone(
                                    context, File(
                                        Environment.getExternalStoragePublicDirectory(
                                            Environment.DIRECTORY_MUSIC
                                        ), ringtone.name + ".mp3"
                                    ), RingtoneManager.TYPE_RINGTONE
                                )
                            } else {
                                Downloader.ringtoneSetStatePub.value = RingtoneSetState.Setting
                                Downloader
                                    .downloadFromURL(ringtone)
                                    .invokeOnCompletion {
                                        Downloader.setRingtone(
                                            context, File(
                                                Environment.getExternalStoragePublicDirectory(
                                                    Environment.DIRECTORY_MUSIC
                                                ), ringtone.name + ".mp3"
                                            ), RingtoneManager.TYPE_RINGTONE
                                        )
                                    }
                            }
                        },
                    imageVector = Icons.Default.RingVolume,
                    text = if (Downloader.ringtoneSetStatePub.value is RingtoneSetState.Set) "Ringtone set" else "Set as ringtone",
                    ringtoneSetState = Downloader.ringtoneSetStatePub.value
                )
                Spacer(modifier = Modifier.height(22.dp))
                RingtoneOptions(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clickable(enabled = Downloader.alarmToneSetStatePub.value !is RingtoneSetState.Setting) {

                            if (networkStatus !is ConnectivityObserver.Status.Available) {
                                Alerts.networkAlert(context)
                                return@clickable
                            }

//                            if (!adSeen) {
//                                enabled = true
//                                return@clickable
//                            }

                            if (isLowerSDK) {
                                if (!writeExternalPermission.status.isGranted) {
                                    lowSDKPermission = true
                                    if (!Settings.System.canWrite(context)) {
                                        writePermissionAlert = true
                                    }
                                    return@clickable
                                }
                            } else {
                                if (!Settings.System.canWrite(context)) {
                                    writePermissionAlert = true
                                    return@clickable
                                }
                            }

                            if (Downloader.downloadingStatePub.value is DownloadState.Downloaded
                                && Downloader.downloadingStatePub.value !is DownloadState.Started
                            ) {
                                Downloader.setRingtone(
                                    context, File(
                                        Environment.getExternalStoragePublicDirectory(
                                            Environment.DIRECTORY_MUSIC
                                        ), ringtone.name + ".mp3"
                                    ), RingtoneManager.TYPE_ALARM
                                )
                            } else {
                                Downloader.alarmToneSetStatePub.value = RingtoneSetState.Setting
                                Downloader
                                    .downloadFromURL(ringtone)
                                    .invokeOnCompletion {
                                        Downloader.setRingtone(
                                            context, File(
                                                Environment.getExternalStoragePublicDirectory(
                                                    Environment.DIRECTORY_MUSIC
                                                ), ringtone.name + ".mp3"
                                            ), RingtoneManager.TYPE_ALARM
                                        )
                                    }
                            }
                        },
                    imageVector = Icons.Default.Alarm,
                    ringtoneSetState = Downloader.alarmToneSetStatePub.value,
                    text = if (Downloader.alarmToneSetStatePub.value is RingtoneSetState.Set) "Alarm tone set" else "Set as alarm tone"
                )
                Spacer(modifier = Modifier.height(22.dp))
                RingtoneOptions(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clickable(enabled = Downloader.notificationToneSetStatePub.value !is RingtoneSetState.Setting) {

                            if (networkStatus !is ConnectivityObserver.Status.Available) {
                                Alerts.networkAlert(context)
                                return@clickable
                            }

//                            if (!adSeen) {
//                                enabled = true
//                                return@clickable
//                            }

                            if (isLowerSDK) {
                                if (!writeExternalPermission.status.isGranted) {
                                    lowSDKPermission = true
                                    if (!Settings.System.canWrite(context)) {
                                        writePermissionAlert = true
                                    }
                                    return@clickable
                                }
                            } else {
                                if (!Settings.System.canWrite(context)) {
                                    writePermissionAlert = true
                                    return@clickable
                                }
                            }

                            if (Downloader.downloadingStatePub.value is DownloadState.Downloaded
                                && Downloader.downloadingStatePub.value !is DownloadState.Started
                            ) {
                                Downloader.setRingtone(
                                    context, File(
                                        Environment.getExternalStoragePublicDirectory(
                                            Environment.DIRECTORY_MUSIC
                                        ), ringtone.name + ".mp3"
                                    ), RingtoneManager.TYPE_NOTIFICATION
                                )
                            } else {
                                Downloader.notificationToneSetStatePub.value =
                                    RingtoneSetState.Setting
                                Downloader
                                    .downloadFromURL(ringtone)
                                    .invokeOnCompletion {
                                        Downloader.setRingtone(
                                            context, File(
                                                Environment.getExternalStoragePublicDirectory(
                                                    Environment.DIRECTORY_MUSIC
                                                ), ringtone.name + ".mp3"
                                            ), RingtoneManager.TYPE_NOTIFICATION
                                        )
                                    }
                            }
                        },
                    imageVector = Icons.Default.Notifications,
                    ringtoneSetState = Downloader.notificationToneSetStatePub.value,
                    text = if (Downloader.notificationToneSetStatePub.value is RingtoneSetState.Set) "Notification tone set" else "Set as notification tone",
                )
                Spacer(modifier = Modifier.height(22.dp))
                RingtoneOptions(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clickable(enabled = Downloader.contactRingtoneSetStatePub.value !is RingtoneSetState.Setting) {

                            if (networkStatus !is ConnectivityObserver.Status.Available) {
                                Alerts.networkAlert(context)
                                return@clickable
                            }

//                            if (!adSeen) {
//                                enabled = true
//                                return@clickable
//                            }

                            if (!contactPermission.status.isGranted) {
                                contactPermissionAsk = true
                                return@clickable
                            }

                            launcher.launch()
                        },
                    imageVector = Icons.Default.Contacts,
                    isForContact = true,
                    ringtoneSetState = Downloader.contactRingtoneSetStatePub.value,
                    text = "Set as contact tone"
                )
                Spacer(modifier = Modifier.height(22.dp))
                RingtoneOptions(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clickable(enabled = Downloader.downloadingStatePub.value !is DownloadState.Started) {

                            if (networkStatus !is ConnectivityObserver.Status.Available) {
                                Alerts.networkAlert(context)
                                return@clickable
                            }

//                            if (!adSeen) {
//                                enabled = true
//                                return@clickable
//                            }

                            if (isLowerSDK) {
                                if (!writeExternalPermission.status.isGranted) {
                                    lowSDKPermission = true
                                    if (!Settings.System.canWrite(context)) {
                                        writePermissionAlert = true
                                    }
                                    return@clickable
                                }
                            } else {
                                if (!Settings.System.canWrite(context)) {
                                    writePermissionAlert = true
                                    return@clickable
                                }
                            }

                            if (Downloader.downloadExists(ringtone.name.toString())) {
                                Downloader.downloadingStatePub.value = DownloadState.Downloaded
                                MainUtil.makeToast(context, "Downloaded", Toast.LENGTH_SHORT)
                                return@clickable
                            } else {
                                Downloader.downloadFromURL(ringtone)
                                MainUtil.makeToast(context, "You can find downloads in your music folder", Toast.LENGTH_SHORT)
                            }
                        },
                    isFromDownload = true,
                    imageVector = Icons.Default.Download,
                    text = "Download"
                )

//                Button(modifier = Modifier
//                    .padding(top = 24.dp)
//                    .alpha(if (adSeen) 0f else 1f)
//                    .background(MaterialTheme.colors.primary)
//                    .graphicsLayer {
//                        scaleX = if (enabled) scale else 1f
//                        scaleY = if (enabled) scale else 1f
//                    }, onClick = {
//                        if (!adSeen) {
//                            try {
//                                adLoading = true
//                                AdManager.loadRewardedAd(context){ bool ->
//                                    if (bool){
//                                        AdManager.mRewardedAd?.show(context as Activity){
//                                            adSeen = true
//                                        }
//                                    }
//                                }
//                            }catch (e : Exception){
//                                e.stackTrace
//                                adSeen = false
//                                adLoading = false
//                            }
//                        }
//                    },
//                    enabled = !adLoading ) {
//                    if(adLoading){
//                        Text(text = "Loading ad please wait...",
//                            textAlign = TextAlign.Center,
//                            color = Color.White)
//                    }else{
//                        Text(text = "Watch an ad and unlock all options",
//                            textAlign = TextAlign.Center,
//                            color = Color.White)
//                    }
//                }
            }


        }
    }
}
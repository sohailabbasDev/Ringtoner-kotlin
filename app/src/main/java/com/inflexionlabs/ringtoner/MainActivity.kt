package com.inflexionlabs.ringtoner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.ads.MobileAds
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.inflexionlabs.ringtoner.firebase_database.RingtonesViewModel
import com.inflexionlabs.ringtoner.operations.AdManager
import com.inflexionlabs.ringtoner.operations.RingtonePlayer
import com.inflexionlabs.ringtoner.operations.connectivity.ConnectivityObserver
import com.inflexionlabs.ringtoner.operations.connectivity.NetworkConnectivityObserver
import com.inflexionlabs.ringtoner.presentation.bottom_bar.BottomBar
import com.inflexionlabs.ringtoner.presentation.navigation.HomeNavGraph
import com.inflexionlabs.ringtoner.ui.theme.RingtonerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@ExperimentalFoundationApi
@ExperimentalPermissionsApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var connectivityObserver: ConnectivityObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition { RingtonesViewModel.isLoading.value }
        }
        super.onCreate(savedInstanceState)

        connectivityObserver = NetworkConnectivityObserver(applicationContext)

        setContent {
            RingtonerTheme {

//                FirebaseApp.initializeApp(/*context=*/this)
//                val firebaseAppCheck = FirebaseAppCheck.getInstance()
//                firebaseAppCheck.installAppCheckProviderFactory(
//                    PlayIntegrityAppCheckProviderFactory.getInstance()
//                )

                val appUpdateManager = AppUpdateManagerFactory.create(this)

                // Returns an intent object that you use to check for an update.
                val appUpdateInfoTask = appUpdateManager.appUpdateInfo

                val status by connectivityObserver.observe().collectAsState(
                    initial = ConnectivityObserver.Status.Unavailable
                )

                // Checks that the platform will allow the specified type of update.
                appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        // This example applies an immediate update. To apply a flexible update
                        // instead, pass in AppUpdateType.FLEXIBLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
                    ) {
                        // Request the update.
                        try {
                            appUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo,
                                AppUpdateType.IMMEDIATE /*AppUpdateType.FLEXIBLE*/,
                                this@MainActivity,
                                MainUtil.RC_APP_UPDATE
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                MobileAds.initialize(this)

                val navController = rememberNavController()
                val scrollState = rememberLazyListState()
                val state = remember {
                    derivedStateOf { scrollState.firstVisibleItemIndex < 12 }
                }

                Scaffold(bottomBar = {  AnimatedVisibility(visible = state.value, enter = fadeIn(), exit = fadeOut() ){
                    BottomBar(navController = navController)
                } } )
                {it.calculateBottomPadding()
                    HomeNavGraph(navController = navController, scrollState, status)
                }

                LaunchedEffect(key1 = true){
                    repeat(5){
                        delay(60.seconds)
                        try {
                            AdManager.showInterstitial(this@MainActivity)

                        }catch (e : Exception){
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        RingtonePlayer.destroyPlayer()
    }

}
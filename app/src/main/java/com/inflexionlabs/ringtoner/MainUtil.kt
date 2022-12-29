package com.inflexionlabs.ringtoner

import android.content.Context
import android.widget.Toast

object MainUtil {
    const val RC_APP_UPDATE = 11
    const val REWARDED_AD = "ca-app-pub-5486922671655114/5129879119"
    const val INTERSTITIAL_AD = "ca-app-pub-5486922671655114/2523402234"

    private var currentToast: Toast? = null
    private var currentMessage: String? = null

    fun makeToast(context: Context, message: String, duration: Int){
        if (message == currentMessage) {
            currentToast?.cancel()
        }
        currentToast = Toast.makeText(context, message, duration)
        currentToast?.show()

        currentMessage = message
    }

}
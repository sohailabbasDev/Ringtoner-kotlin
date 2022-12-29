package com.inflexionlabs.ringtoner.operations

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.inflexionlabs.ringtoner.MainUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

object AdManager {

    private val rewardedAdJob = Job()
    private val rewardedAdScope = CoroutineScope(rewardedAdJob)

    var mInterstitialAd: InterstitialAd? = null
    var mRewardedAd: RewardedAd? = null

    fun showInterstitial(context: Context) {
        val adRequest = AdRequest.Builder().build()

        try {
            InterstitialAd.load(context, MainUtil.INTERSTITIAL_AD.trim(),
                adRequest, object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        Log.d("ad", adError.toString())
                        mInterstitialAd = null
                    }

                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        Log.d("ads", "Ad was loaded.")
                        mInterstitialAd = interstitialAd
                        mInterstitialAd?.show(context as Activity)
                        mInterstitialAd = null
                    }
                }
            )
        }catch (e : Exception){
            e.stackTrace
        }
    }

    fun loadRewardedAd(context: Context, result: (Boolean) -> Unit) = rewardedAdScope.launch(Dispatchers.Main){
        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(context , MainUtil.REWARDED_AD.trim(), adRequest, object : RewardedAdLoadCallback(){
            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                // Handle the error.
                mRewardedAd = null
                result.invoke(false)
                throw Exception()
            }

            override fun onAdLoaded(rewardedAd: RewardedAd) {
                mRewardedAd = rewardedAd
                result.invoke(true)
            }
        })
    }

//    fun showRewarded(context: Context, result: (Boolean) -> Unit) = rewardedAdScope.launch (Dispatchers.Main){
//
//        val adRequest = AdRequest.Builder().build()
//
//        RewardedAd.load(context, MainUtil.REWARDED_AD.trim(),
//            adRequest, object : RewardedAdLoadCallback() {
//                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
//                    // Handle the error.
//                    mRewardedAd = null
//                    throw Exception()
//                }
//
//                override fun onAdLoaded(rewardedAd: RewardedAd) {
//                    mRewardedAd = rewardedAd
//                    mRewardedAd!!.show(context as Activity
//                    ) {
//                        result.invoke(true)
//                    }
//                }
//        }  )
//
//    }

}
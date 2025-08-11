package com.example.mytask.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.*
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

object AdManager {

    // Add banner ad unit ID here so BannerAd composable can use it
    const val bannerAdUnitId = "ca-app-pub-3940256099942544/6300978111" // Test Banner Ad Unit

    private var interstitialAd: InterstitialAd? = null
    private var appOpenAd: AppOpenAd? = null
    private var rewardedAd: RewardedAd? = null

    fun init(context: Context) {
        MobileAds.initialize(context)
    }

    fun getAdRequest(): AdRequest = AdRequest.Builder().build()

    // ---------------- Interstitial ----------------
    fun loadInterstitial(context: Context) {
        InterstitialAd.load(
            context,
            "ca-app-pub-3940256099942544/1033173712", // Test Interstitial Ad Unit
            getAdRequest(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                    Log.e("AdManager", "Interstitial failed: ${error.message}")
                }
            }
        )
    }

    fun showInterstitial(activity: Activity, onDismiss: () -> Unit) {
        interstitialAd?.let { ad ->
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    loadInterstitial(activity)
                    onDismiss()
                }
            }
            ad.show(activity)
        } ?: onDismiss()
    }

    // ---------------- Native ----------------
    fun loadNativeAd(context: Context, onAdLoaded: (NativeAd) -> Unit) {
        val adLoader = AdLoader.Builder(
            context,
            "ca-app-pub-3940256099942544/2247696110" // Test Native Ad Unit
        )
            .forNativeAd { nativeAd ->
                onAdLoaded(nativeAd)
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e("AdManager", "Native failed: ${error.message}")
                }
            })
            .withNativeAdOptions(NativeAdOptions.Builder().build())
            .build()

        adLoader.loadAd(getAdRequest())
    }

    // ---------------- Rewarded ----------------
    fun loadRewarded(context: Context) {
        RewardedAd.load(
            context,
            "ca-app-pub-3940256099942544/5224354917", // Test Rewarded Ad Unit
            getAdRequest(),
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    Log.d("AdManager", "Rewarded ad loaded.")
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    rewardedAd = null
                    Log.e("AdManager", "Rewarded failed: ${error.message}")
                }
            }
        )
    }

    fun showRewarded(activity: Activity, onRewardEarned: (RewardItem) -> Unit) {
        rewardedAd?.let { ad ->
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    rewardedAd = null
                    loadRewarded(activity) // Preload again
                }
            }
            ad.show(activity) { rewardItem ->
                onRewardEarned(rewardItem)
            }
        } ?: run {
            Log.e("AdManager", "Rewarded ad not ready")
        }
    }
}
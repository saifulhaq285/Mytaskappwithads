package com.example.mytask.ads

import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun BannerAd(modifier: Modifier = Modifier, adUnitId: String? = null) {
    val context = LocalContext.current
    val resolvedUnitId = adUnitId ?: AdManager.bannerAdUnitId // ✅ Use a new variable

    AndroidView(
        factory = {
            AdView(it).apply {
                setAdSize(AdSize.BANNER) // ✅ Fix for adSize setter
                this.adUnitId = resolvedUnitId // ✅ Assign to AdView property, not function parameter
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                loadAd(AdRequest.Builder().build())
            }
        },
        modifier = modifier
    )
}
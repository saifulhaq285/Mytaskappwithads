package com.example.mytask

import android.app.Application
import com.example.mytask.ads.AdManager

class MyTaskApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // ✅ Initialize Mobile Ads and preload App Open Ad immediately
        AdManager.init(this)

    }
}
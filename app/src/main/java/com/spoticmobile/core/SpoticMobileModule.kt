package com.spoticmobile.core

import android.app.Application
import com.spoticmobile.hooks.PremiumHooks
import com.spoticmobile.hooks.ShuffleHooks
import com.spoticmobile.hooks.SkipLimitHooks
import com.spoticmobile.hooks.AdBlockerHooks
import com.spoticmobile.hooks.AudioQualityHooks
import com.spoticmobile.ui.MainActivity
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

class SpoticMobileModule : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != "com.spotify.music") return

        XposedBridge.log("[SpoticMobile] Module loaded")

        // Get context to read preferences
        val context = try {
            val activityThread = Class.forName("android.app.ActivityThread")
            val method = activityThread.getMethod("currentApplication")
            method.invoke(null) as Application
        } catch (e: Throwable) {
            XposedBridge.log("[SpoticMobile] Failed to get context: $e")
            null
        }

        if (context == null) {
            XposedBridge.log("[SpoticMobile] No context, loading all hooks")
            applyAllHooks(lpparam)
            return
        }

        // Apply hooks based on preferences
        if (MainActivity.isEnabled(context, "premium_enabled", true)) {
            try {
                PremiumHooks.apply(lpparam)
                XposedBridge.log("[SpoticMobile] Premium hooks applied")
            } catch (e: Throwable) {
                XposedBridge.log("[SpoticMobile] Premium hooks failed: $e")
            }
        }

        if (MainActivity.isEnabled(context, "adblock_enabled", true)) {
            try {
                AdBlockerHooks.apply(lpparam)
                XposedBridge.log("[SpoticMobile] AdBlock hooks applied")
            } catch (e: Throwable) {
                XposedBridge.log("[SpoticMobile] AdBlock hooks failed: $e")
            }
        }

        if (MainActivity.isEnabled(context, "quality_enabled", true)) {
            try {
                AudioQualityHooks.apply(lpparam)
                XposedBridge.log("[SpoticMobile] Audio quality hooks applied")
            } catch (e: Throwable) {
                XposedBridge.log("[SpoticMobile] Audio quality hooks failed: $e")
            }
        }

        // Always apply shuffle and skip (part of premium unlock)
        try {
            ShuffleHooks.apply(lpparam)
            SkipLimitHooks.apply(lpparam)
            XposedBridge.log("[SpoticMobile] Shuffle/Skip hooks applied")
        } catch (e: Throwable) {
            XposedBridge.log("[SpoticMobile] Shuffle/Skip hooks failed: $e")
        }
    }

    private fun applyAllHooks(lpparam: XC_LoadPackage.LoadPackageParam) {
        try { PremiumHooks.apply(lpparam) } catch (_: Throwable) {}
        try { ShuffleHooks.apply(lpparam) } catch (_: Throwable) {}
        try { SkipLimitHooks.apply(lpparam) } catch (_: Throwable) {}
        try { AdBlockerHooks.apply(lpparam) } catch (_: Throwable) {}
        try { AudioQualityHooks.apply(lpparam) } catch (_: Throwable) {}
    }
}

package com.spoticmobile.core

import android.app.Application
import com.spoticmobile.hooks.*
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

class SpoticMobileModule : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != "com.spotify.music") return

        XposedBridge.log("[SpoticMobile] Module loaded")

        val context = try {
            val activityThread = Class.forName("android.app.ActivityThread")
            activityThread.getMethod("currentApplication").invoke(null) as Application
        } catch (e: Throwable) {
            XposedBridge.log("[SpoticMobile] No context: $e")
            null
        }

        val prefs = context?.getSharedPreferences("spoticmobile", 0)

        if (prefs?.getBoolean("premium_enabled", true) != false) {
            try { PremiumHooks.apply(lpparam); XposedBridge.log("[SpoticMobile] Premium OK") } catch (e: Throwable) { XposedBridge.log("[SpoticMobile] Premium FAIL: $e") }
        }
        if (prefs?.getBoolean("adblock_enabled", true) != false) {
            try { AdBlockerHooks.apply(lpparam); XposedBridge.log("[SpoticMobile] AdBlock OK") } catch (e: Throwable) { XposedBridge.log("[SpoticMobile] AdBlock FAIL: $e") }
        }
        if (prefs?.getBoolean("quality_enabled", true) != false) {
            try { AudioQualityHooks.apply(lpparam); XposedBridge.log("[SpoticMobile] Quality OK") } catch (e: Throwable) { XposedBridge.log("[SpoticMobile] Quality FAIL: $e") }
        }

        try { ShuffleHooks.apply(lpparam); SkipLimitHooks.apply(lpparam); XposedBridge.log("[SpoticMobile] Shuffle/Skip OK") } catch (e: Throwable) { XposedBridge.log("[SpoticMobile] Shuffle/Skip FAIL: $e") }
    }
}

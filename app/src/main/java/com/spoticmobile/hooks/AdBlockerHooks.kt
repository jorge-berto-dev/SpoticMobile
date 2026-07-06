package com.spoticmobile.hooks

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

object AdBlockerHooks {

    fun apply(lpparam: XC_LoadPackage.LoadPackageParam) {
        val cl = lpparam.classLoader

        val adClasses = listOf(
            "com.spotify.ads.AdManager",
            "com.spotify.ads.audio.AudioAdManager",
            "com.spotify.ads.audio.AudioAdsManager",
            "com.spotify.music.ads.AudioAdController",
            "com.spotify.player.PlayerAdDelegate"
        )

        for (cls in adClasses) {
            try {
                XposedHelpers.findAndHookMethod(cls, cl, "playAd",
                    object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam) {
                            param.result = null
                        }
                    })
            } catch (_: Throwable) {}

            try {
                XposedHelpers.findAndHookMethod(cls, cl, "showAd",
                    object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam) {
                            param.result = null
                        }
                    })
            } catch (_: Throwable) {}

            try {
                XposedHelpers.findAndHookMethod(cls, cl, "shouldPlayAd",
                    XC_MethodReplacement.returnConstant(false))
            } catch (_: Throwable) {}

            try {
                XposedHelpers.findAndHookMethod(cls, cl, "isAdPlaying",
                    XC_MethodReplacement.returnConstant(false))
            } catch (_: Throwable) {}
        }
    }
}

package com.spoticmobile.hooks

import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

object AudioQualityHooks {

    fun apply(lpparam: XC_LoadPackage.LoadPackageParam) {
        val cl = lpparam.classLoader

        val methods = mapOf(
            "getMaxAvailableQuality" to "very_high",
            "getMaxAudioQuality" to "very_high",
            "isVeryHighQualityAvailable" to true,
            "isLosslessAvailable" to true,
            "isExtremeQualityAvailable" to true,
            "canStreamLossless" to true,
            "canStreamVeryHigh" to true,
            "allowAudioQualityDowngrade" to false
        )

        val classes = listOf(
            "com.spotify.music.settings.AudioQualitySettings",
            "com.spotify.music.features.settings.AudioQualityHelper",
            "com.spotify.player.PlaybackQualityManager"
        )

        for (cls in classes) {
            for ((method, value) in methods) {
                try {
                    XposedHelpers.findAndHookMethod(cls, cl, method,
                        XC_MethodReplacement.returnConstant(value))
                } catch (_: Throwable) {}
            }
        }

        // Expose all quality options
        try {
            XposedHelpers.findAndHookMethod(
                "com.spotify.music.settings.AudioQualitySettings",
                cl, "getAvailableQualities",
                XC_MethodReplacement.returnConstant(
                    listOf("normal", "high", "very_high", "lossless")
                )
            )
        } catch (_: Throwable) {}
    }
}

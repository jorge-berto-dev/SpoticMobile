package com.spoticmobile.hooks

import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

object SkipLimitHooks {

    fun apply(lpparam: XC_LoadPackage.LoadPackageParam) {
        val cl = lpparam.classLoader

        val methods = mapOf(
            "isSkipLimitReached" to false,
            "hasReachedSkipLimit" to false,
            "canSkipNext" to true,
            "canSkipPrevious" to true,
            "canSkipToNext" to true,
            "canSkipToPrevious" to true,
            "isOnDemandInFree" to false,
            "hasOnDemandAccess" to true
        )

        val classes = listOf(
            "com.spotify.player.legacy.PlayerUtil",
            "com.spotify.music.player.PlayerState",
            "com.spotify.connectivity.sessionstate.SessionState"
        )

        for (cls in classes) {
            for ((method, value) in methods) {
                try {
                    XposedHelpers.findAndHookMethod(cls, cl, method,
                        XC_MethodReplacement.returnConstant(value))
                } catch (_: Throwable) {}
            }
        }
    }
}

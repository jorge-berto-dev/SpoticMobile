package com.spoticmobile.hooks

import android.widget.Button
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

object ShuffleHooks {

    fun apply(lpparam: XC_LoadPackage.LoadPackageParam) {
        val cl = lpparam.classLoader

        // Unlock shuffle button
        shuffleClass("com.spotify.encoreconsumermobile.elements.shuffle.ShuffleButtonView", cl)
        shuffleClass("com.spotify.encoreconsumermobile.elements.smartshufflebutton.SmartShuffleButtonView", cl)

        // Remove forced shuffle
        val targets = listOf(
            "com.spotify.smartshuffle.playerimpl.SmartShufflePlayerImpl",
            "com.spotify.player.legacy.PlayerUtil",
            "com.spotify.music.features.player.PlayerNavigationHelper"
        )
        for (cls in targets) {
            try {
                XposedHelpers.findAndHookMethod(cls, cl, "isShuffleForced",
                    XC_MethodReplacement.returnConstant(false))
            } catch (_: Throwable) {}
            try {
                XposedHelpers.findAndHookMethod(cls, cl, "canToggleShuffle",
                    XC_MethodReplacement.returnConstant(true))
            } catch (_: Throwable) {}
        }
    }

    private fun shuffleClass(cls: String, cl: ClassLoader) {
        try {
            XposedHelpers.findAndHookMethod(cls, cl, "setEnabled",
                Boolean::class.javaPrimitiveType,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        param.args[0] = true
                    }
                }
            )
        } catch (_: Throwable) {}
    }
}

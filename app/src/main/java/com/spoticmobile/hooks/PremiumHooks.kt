package com.spoticmobile.hooks

import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

object PremiumHooks {

    fun apply(lpparam: XC_LoadPackage.LoadPackageParam) {
        val cl = lpparam.classLoader

        val sessionState = "com.spotify.connectivity.sessionstate.SessionState"

        try {
            XposedHelpers.findAndHookMethod(
                sessionState, cl, "getProductType",
                XC_MethodReplacement.returnConstant("premium")
            )
        } catch (_: Throwable) {}

        try {
            XposedHelpers.findAndHookMethod(
                sessionState, cl, "getPaymentState",
                XC_MethodReplacement.returnConstant("paid")
            )
        } catch (_: Throwable) {}

        try {
            XposedHelpers.findAndHookMethod(
                sessionState, cl, "getCurrentAccountType",
                XC_MethodReplacement.returnConstant("premium")
            )
        } catch (_: Throwable) {}
    }
}

package com.example.crypto

import android.annotation.TargetApi
import android.app.KeyguardManager
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AlertDialog


@TargetApi(Build.VERSION_CODES.M)
class SystemServices(private val context: Context) {

    companion object {
        fun hasMarshmallow() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    private val keyguardManager: KeyguardManager

    fun isDeviceSecure(): Boolean = if (hasMarshmallow()) keyguardManager.isDeviceSecure else keyguardManager.isKeyguardSecure


    init {
        keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
    }


    fun Context.openLockScreenSettings() {
        val intent = Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD)
        startActivity(intent)
    }

    fun Context.openSecuritySettings() {
        val intent = Intent(Settings.ACTION_SECURITY_SETTINGS)
        startActivity(intent)
    }
}
package com.appsfourlife.draftogo.helpers

import android.content.Context
import android.content.pm.PackageManager
import android.provider.Settings
import android.text.TextUtils
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import com.appsfourlife.draftogo.App

object HelperPermissions {

    fun isPermissionGranted(permission: String): Boolean{
        if (ContextCompat.checkSelfPermission(App.context, permission) ==
            PackageManager.PERMISSION_GRANTED
        ){
            return true
        }
         return false
    }

    fun requestPermission(permission: String, requestPermissionLauncher: ActivityResultLauncher<String>){
        if (!isPermissionGranted(permission = permission))
            requestPermissionLauncher.launch(permission)
    }

    fun isAccessibilityEnabled(context: Context): Boolean {
        var accessibilityEnabled = 0;
        val accessibilityFound = false;
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                context.contentResolver,
                android.provider.Settings.Secure.ACCESSIBILITY_ENABLED
            );
        } catch (e: Settings.SettingNotFoundException) {
        }

        val mStringColonSplitter = TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {

            val settingValue = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            );
            if (settingValue != null) {
                val splitter = mStringColonSplitter;
                splitter.setString(settingValue);
                while (splitter.hasNext()) {
                    val accessabilityService = splitter.next();
                    if (accessabilityService == "com.appsfourlife.draftogo/com.appsfourlife.draftogo.MyAccessibilityService") {
                        return true
                    }
                }
            }
        } else {
        }
        return accessibilityFound;
    }

}
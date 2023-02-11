package com.appsfourlife.draftogo.helpers

import android.content.pm.PackageManager
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
}
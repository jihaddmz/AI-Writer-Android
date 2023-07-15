package com.appsfourlife.draftogo.feature_generate_video.presentations

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.AppBarTransparent
import com.appsfourlife.draftogo.components.MyOutlinedButton
import com.appsfourlife.draftogo.components.MySpacer
import com.appsfourlife.draftogo.components.VideoView
import com.appsfourlife.draftogo.feature_generate_video.utils.NotifiersVideo
import com.appsfourlife.draftogo.helpers.HelperUI
import com.appsfourlife.draftogo.helpers.Helpers
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import com.appsfourlife.draftogo.util.BottomNavScreens
import com.appsfourlife.draftogo.util.SettingsNotifier
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout

@Composable
fun ScreenVideoGenerated(navController: NavController) {

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(bottom = SpacersSize.small), verticalArrangement = Arrangement.SpaceBetween) {

        AppBarTransparent(title = stringResource(id = R.string.video_is_generated)) {
            navController.navigate(BottomNavScreens.Video.route)
        }

        MySpacer(type = "medium")

        VideoView(
            uri = NotifiersVideo.videoGeneratedIUri!!,
            true,
            resizeMode1 = AspectRatioFrameLayout.RESIZE_MODE_FIT
        )

        MySpacer(type = "medium")

        MyOutlinedButton(
            text = stringResource(id = R.string.download_video),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SpacersSize.small)
        ) {
            if (!SettingsNotifier.isConnected.value){
                HelperUI.showToast(context = context, msg = App.getTextFromString(R.string.no_connection))
                return@MyOutlinedButton
            }

            downloadFromUrl(url = NotifiersVideo.videoGeneratedIUri!!, context)
        }
    }
}

//fun requestVideoGeneration() {
//    val client = OkHttpClient()
//
//    val MEDIA_TYPE = "application/json".toMediaType()
//
////    val requestBody = "{template_id: 9f4a51a0-f9b5-43a5-803d-1829db78c140,modifications: {ecf1a01d-ff16-4b5f-a58c-a4998b02e502: https://cdn.creatomate.com/files/assets/7347c3b7-e1a8-4439-96f1-f3dfc95c3d28,Text-1:Your Text And Video Here,Text-2:Create & Automate\\n[size 150%]Video[/size]}}"
//
//    val jsonObject = JSONObject()
//    jsonObject.put("template_id", "34d66a9b-7569-4a2b-9232-ae05af41320e")
////    jsonObject.put("modifications", JSONObject())
////    jsonObject.put("modifications",
////        JSONObject().put("ecf1a01d-ff16-4b5f-a58c-a4998b02e502", "https://cdn.creatomate.com/files/assets/7347c3b7-e1a8-4439-96f1-f3dfc95c3d28")
////        JSONObject()
////            .put("Text-1", "Your Text And Video Here")
////            .put("Text-2", "Create & Automate")
////            .put("Text", "Your Logo"))
//
//    val request = Request.Builder()
//        .url("https://api.creatomate.com/v1/renders")
//        .post(jsonObject.toString().toRequestBody(MEDIA_TYPE))
//        .header("Authorization", "Bearer c6dbccdd281a431e8186ec898cebca7b090f94a2a4cc9468e0941b4e07b4b85aae573034dc723262a4deb1c60492e44e")
//        .header("Content-Type", "application/json")
//        .build()
//
//    client.newCall(request).execute().use { response ->
//        if (!response.isSuccessful)
//            Helpers.logD("exception ${response.message}")
////            throw IOException("Unexpected code $response")
//
//        Helpers.logD("response is ${response.body!!.string()}")
//    }
//
//}

private fun downloadFromUrl(url: String, context: Context) {
    try {
        val request = DownloadManager.Request(url.toUri())
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle(App.getTextFromString(R.string.app_name))
        request.setDescription(App.getTextFromString(R.string.downloading_your_video))
        request.setMimeType("video/mp4")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            "${System.currentTimeMillis()}.mp4"
        )
        val downloadManager = context.getSystemService(DownloadManager::class.java) as DownloadManager
        downloadManager.enqueue(request)
    } catch (exception: Exception) {
        Helpers.logD("Exception ${exception.message}")
    }
}
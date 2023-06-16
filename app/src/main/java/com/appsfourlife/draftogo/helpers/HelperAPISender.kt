package com.appsfourlife.draftogo.helpers

import com.google.firebase.auth.FirebaseUser
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

object HelperAPISender {

    fun subscribeUser(user: FirebaseUser) {
        val client = OkHttpClient()

        val MEDIA_TYPE = "application/json".toMediaType()

        val jsonObject = JSONObject()
        jsonObject.put("email", user.email)
        jsonObject.put("firstname", user.displayName!!.split(" ")[0])
        jsonObject.put("lastname", user.displayName!!.split(" ")[1])
        jsonObject.put("groups", JSONArray().put("ej8m6l"))
        if (!user.phoneNumber.isNullOrEmpty())
            jsonObject.put("phone", user.phoneNumber)
        jsonObject.put("trigger_automation", false)

        Helpers.logD("request $jsonObject")

        val request = Request.Builder()
            .url("https://api.sender.net/v2/subscribers")
            .post(jsonObject.toString().toRequestBody(MEDIA_TYPE))
            .header(
                "Authorization",
                "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiYzBiNmVkZmNjYTY5NTM4ZWE3NGFhODUxNjVkOWM3N2I2MTZmZWJhMzgzNDIxMDYxY2NjNmU0MTMxMzRhYzA5MjE2ZTAwNmRiMzExM2JhMjAiLCJpYXQiOiIxNjg2OTIyMTQyLjUxMjM0OSIsIm5iZiI6IjE2ODY5MjIxNDIuNTEyMzUzIiwiZXhwIjoiNDg0MDUyMjE0Mi41MDkyNjUiLCJzdWIiOiI3OTgwMTciLCJzY29wZXMiOltdfQ.tIq0lD8u9oPfYR-kV1dqyrLWDJw2fm4REgFNivDhZFMRpT0bZMU2tvG5-tnOtUVteQ57MiH7mBjAXlM0Boy4Nw"
            )
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .build()

        client.newCall(request).execute().use { response ->
            if (response.isSuccessful)
                HelperSharedPreference.setBool(HelperSharedPreference.SP_SETTINGS, HelperSharedPreference.SP_SETTINGS_SUBSCRIBED_TO_EMAIL_SENDER, true)
        }
    }

    fun unsubscribe(user: FirebaseUser) {
        val client = OkHttpClient()

        val MEDIA_TYPE = "application/json".toMediaType()

        val jsonObject = JSONObject()
        jsonObject.put("firstname", user.displayName!!.split(" ")[0])
        jsonObject.put("lastname", user.displayName!!.split(" ")[1])
        jsonObject.put("subscriber_status", "UNSUBSCRIBED")
        if (!user.phoneNumber.isNullOrEmpty())
            jsonObject.put("phone", user.phoneNumber)
        jsonObject.put("trigger_automation", false)


        val request = Request.Builder()
            .url("https://api.sender.net/v2/subscribers/${user.email}")
            .patch(jsonObject.toString().toRequestBody(MEDIA_TYPE))
            .header(
                "Authorization",
                "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiYzBiNmVkZmNjYTY5NTM4ZWE3NGFhODUxNjVkOWM3N2I2MTZmZWJhMzgzNDIxMDYxY2NjNmU0MTMxMzRhYzA5MjE2ZTAwNmRiMzExM2JhMjAiLCJpYXQiOiIxNjg2OTIyMTQyLjUxMjM0OSIsIm5iZiI6IjE2ODY5MjIxNDIuNTEyMzUzIiwiZXhwIjoiNDg0MDUyMjE0Mi41MDkyNjUiLCJzdWIiOiI3OTgwMTciLCJzY29wZXMiOltdfQ.tIq0lD8u9oPfYR-kV1dqyrLWDJw2fm4REgFNivDhZFMRpT0bZMU2tvG5-tnOtUVteQ57MiH7mBjAXlM0Boy4Nw"
            )
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .build()

        client.newCall(request).execute().use { response ->
        }
    }
}
package com.appsfourlife.draftogo.feature_generate_video.presentations

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.components.AppBarTransparent
import com.appsfourlife.draftogo.components.MyCustomDialog
import com.appsfourlife.draftogo.components.MyLottieAnim
import com.appsfourlife.draftogo.components.MyOutlinedButton
import com.appsfourlife.draftogo.components.MyOutlinedTextField
import com.appsfourlife.draftogo.components.MySpacer
import com.appsfourlife.draftogo.components.MyTextTitle
import com.appsfourlife.draftogo.feature_generate_video.components.ItemVideoTemplate
import com.appsfourlife.draftogo.feature_generate_video.models.ModelVideoTemplate
import com.appsfourlife.draftogo.feature_generate_video.utils.NotifiersVideo
import com.appsfourlife.draftogo.helpers.HelperFirebaseDatabase
import com.appsfourlife.draftogo.helpers.HelperUI
import com.appsfourlife.draftogo.util.BottomNavScreens
import com.appsfourlife.draftogo.util.Screens
import com.appsfourlife.draftogo.util.SettingsNotifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

@Composable
fun ScreenVideoTemplates(navController: NavController) {

    val coroutineScope = rememberCoroutineScope()

    val listOfModelVideoTemplates = remember {
        mutableStateListOf<ModelVideoTemplate>()
    }

    val showDialogVideoGeneration = remember {
        mutableStateOf(false)
    }

    val clickedVideoTemplate = remember {
        mutableStateOf(ModelVideoTemplate("", "", "", hashMapOf()))
    }

    val context = LocalContext.current

    LaunchedEffect(key1 = true, block = {
        coroutineScope.launch(Dispatchers.IO) {
            if (SettingsNotifier.isConnected.value) {
                HelperFirebaseDatabase.fetchVideoTemplates { documentSnapshot ->
                    val id = documentSnapshot.id
                    val bearer = documentSnapshot.getString("bearer")
                    val exampleUri = documentSnapshot.getString("example_url")


                    val mapOfPlaceholders =
                        documentSnapshot.get("map_of_placeholders") as HashMap<Any, Any>

                    listOfModelVideoTemplates.add(
                        ModelVideoTemplate(
                            id,
                            bearer!!,
                            exampleUri!!,
                            mapOfPlaceholders
                        )
                    )
                }
            } else {
                HelperUI.showToast(
                    context = context,
                    msg = App.getTextFromString(R.string.no_connection)
                )
            }
        }
    })

    Column(modifier = Modifier.fillMaxSize()) {

        AppBarTransparent(title = stringResource(id = R.string.video_templates)) {
            navController.navigate(BottomNavScreens.Dashboard.route)
        }

        if (listOfModelVideoTemplates.size == 0) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                MyLottieAnim(lottieID = R.raw.empty_box)
            }
        }

        if (showDialogVideoGeneration.value)
            DialogVideoGeneration(
                clickedVideoTemplate = clickedVideoTemplate.value,
                showDialog = showDialogVideoGeneration,
                navController
            )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(
                count = listOfModelVideoTemplates.size,
            ) { index ->

                val currentTemplate = listOfModelVideoTemplates[index]

                ItemVideoTemplate(modelVideoTemplate = currentTemplate, modifier = Modifier) {
                    clickedVideoTemplate.value = it
                    showDialogVideoGeneration.value = true
                }

                MySpacer(type = "medium")
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun DialogVideoGeneration(
    clickedVideoTemplate: ModelVideoTemplate,
    showDialog: MutableState<Boolean>,
    navController: NavController
) {
    val showProgress = remember {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    val keyboardController = LocalSoftwareKeyboardController.current

    MyCustomDialog(showDialog = showDialog) {

        val modifier = remember {
            mutableStateOf(Modifier)
        }
        Column(
            modifier = modifier.value
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            MyTextTitle(
                text = stringResource(id = R.string.provide_your_inputs),
                fontWeight = FontWeight.Bold
            )

            MySpacer(type = "small")

            if (showProgress.value) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())

            MySpacer(type = "small")

            LazyColumn {
                items(clickedVideoTemplate.mapOfPlaceholders.size) { index ->
                    val value =
                        clickedVideoTemplate.mapOfPlaceholders.values.elementAt(index) as String
                    val key =
                        clickedVideoTemplate.mapOfPlaceholders.keys.elementAt(index) as String

                    val input = remember {
                        mutableStateOf(
                            clickedVideoTemplate.mapOfPlaceholders.values.elementAt(
                                index
                            ) as String
                        )
                    }

                    MyOutlinedTextField(
                        placeHolder = value,
                        value = input,
                        onValueChange = {
                            clickedVideoTemplate.mapOfPlaceholders[key] = it
                        }, trailingIconID = R.drawable.clear, onTrailingIconClick = {
                            input.value = ""
                        })

                    MySpacer(type = "medium")
                }
            }

            MySpacer(type = "small")

            MyOutlinedButton(text = stringResource(id = R.string.generate)) {
                if (!SettingsNotifier.isConnected.value) {
                    HelperUI.showToast(
                        context = context,
                        msg = App.getTextFromString(R.string.no_connection)
                    )
                    return@MyOutlinedButton
                }

                keyboardController?.hide()

                showProgress.value = true
                coroutineScope.launch(Dispatchers.IO) {
                    requestVideoGeneration(
                        modelVideoTemplate = clickedVideoTemplate,
                        onSuccessListener = { uri ->
                            HelperFirebaseDatabase.incrementNbOfVideosGenerated()
                            showProgress.value = false
                            NotifiersVideo.videoGeneratedIUri =
                                uri
                            showDialog.value = false
                            coroutineScope.launch(Dispatchers.Main) {
                                navController.navigate(Screens.ScreenVideoGenerator.route)
                            }
                        },
                        onFailureListener = {

                        })
                }
            }
        }
    }
}

fun requestVideoGeneration(
    modelVideoTemplate: ModelVideoTemplate,
    onSuccessListener: (String) -> Unit,
    onFailureListener: (String) -> Unit
) {
    val client = OkHttpClient()

    val MEDIA_TYPE = "application/json".toMediaType()

    val jsonObject = JSONObject()
    jsonObject.put("template_id", modelVideoTemplate.id)

    val jsonObject1 = JSONObject()
    modelVideoTemplate.mapOfPlaceholders.forEach { any, u ->
        jsonObject1.put(any as String, u as String)
    }
    jsonObject.put("modifications", jsonObject1)

    val request = Request.Builder()
        .url("https://api.creatomate.com/v1/renders")
        .post(jsonObject.toString().toRequestBody(MEDIA_TYPE))
        .header(
            "Authorization",
            "Bearer ${modelVideoTemplate.bearer}"
        )
        .header("Content-Type", "application/json")
        .build()

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) {
            onFailureListener(response.message)
            return@use
        }

        onSuccessListener(
            JSONArray(response.peekBody(2048).string()).getJSONObject(0).getString("url")
        )
    }
}
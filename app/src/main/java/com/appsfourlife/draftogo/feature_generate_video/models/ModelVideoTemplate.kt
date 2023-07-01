package com.appsfourlife.draftogo.feature_generate_video.models

data class ModelVideoTemplate(
    val id: String,
    val bearer: String,
    val exampleUrl: String,
    val mapOfPlaceholders: HashMap<Any, Any>
)

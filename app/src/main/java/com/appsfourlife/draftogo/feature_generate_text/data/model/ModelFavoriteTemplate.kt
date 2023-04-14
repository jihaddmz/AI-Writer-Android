package com.appsfourlife.draftogo.feature_generate_text.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_favoritetemplates")
data class ModelFavoriteTemplate(
    @PrimaryKey val query: String,
    val iconID: Int?,
    val imageUrl: String?,
)

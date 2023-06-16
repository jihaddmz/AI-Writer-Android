package com.appsfourlife.draftogo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_favoritetemplates")
data class ModelFavoriteTemplate(
    @PrimaryKey val query: String,
    val iconID: Int?,
    val imageUrl: String?,
)
